package com.marketplace.auth.services;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.marketplace.auth.parsers.RSAKeyParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

    private static final String ROLES_CLAIM = "roles";

    private final RSAKeyParser keyParser;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${jwt.private-path}")
    private Resource privateKeyResource;

    @Value("${jwt.public-path}")
    private Resource publicKeyResource;

    private RSAPrivateKey privateKey;

    private RSAPublicKey publicKey;

    public JwtService(RSAKeyParser rsaKeyParser) {
        this.keyParser = rsaKeyParser;
    }

    @PostConstruct
    public void init() throws Exception {
        privateKey = keyParser.loadPrivate(privateKeyResource);
        publicKey = keyParser.loadPublic(publicKeyResource);
    }

    public String generateAccessToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim(ROLES_CLAIM, roles)
                .claim("typ", "ACCESS")
                .issuer("auth-service")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("typ", "REFRESH")
                .issuer("auth-service")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public boolean validateToken(String token) {
        if (token.isEmpty() || token == null) {
            log.warn("jwt is empty");
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object rolesObj = claims.get(ROLES_CLAIM);

        if (rolesObj instanceof List) {
            return ((List<?>) rolesObj).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();

    }

    public boolean isExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        boolean isUserValid = username.equals(userDetails.getUsername());

        boolean isExpired = isExpired(token);

        return isUserValid && !isExpired;
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isUserValid = username.equals(userDetails.getUsername());

        boolean isExpired = isExpired(token);

        return isUserValid && !isExpired;
    }
}
