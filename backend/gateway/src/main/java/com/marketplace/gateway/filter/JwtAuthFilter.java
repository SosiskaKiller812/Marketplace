package com.marketplace.gateway.filter;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.marketplace.gateway.parser.RSAKeyParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class JwtAuthFilter implements GlobalFilter {

  private static final String ROLES_CLAIM = "roles";
  private static final String TYPE_CLAIM = "typ";
  private static final String ACCESS_TYPE = "ACCESS";
  private static final String BEARER = "Bearer ";
  private static final String ISSUER = "AUTH-SERVICE";

  private final RSAKeyParser keyParser;

  @Value("${jwt.public-path}")
  private Resource publicKeyResource;

  private RSAPublicKey publicKey;

  @PostConstruct
  public void init() throws Exception {
    publicKey = keyParser.loadPublic(publicKeyResource);
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getPath().value();

    // public endpoints need to fix
    if (path.startsWith("/api/auth") || path.startsWith("/fallback")) {
      return chain.filter(exchange);
    }

    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith(BEARER)) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    String token = authHeader.substring(7);

    try {
      Claims claims = Jwts.parser()
          .verifyWith(publicKey)
          .requireIssuer("auth-service")
          .build()
          .parseSignedClaims(token)
          .getPayload();

      validate(claims);

      String userId = claims.getSubject();
      String roles = extractRoles(claims);

      ServerHttpRequest request = exchange.getRequest().mutate()
          .header("X-User-Id", userId)
          .header("X-User-Roles", roles)
          .build();

      return chain.filter(exchange.mutate().request(request).build());
    } catch (JwtException e) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }

  private void validate(Claims claims) {
    if (!ACCESS_TYPE.equals(claims.get(TYPE_CLAIM, String.class))) {
      throw new JwtException("Invalid token type");
    }

    if(!ISSUER.equals(claims.getIssuer())){
      throw new JwtException("Invalid issuer");
    }

    if (claims.getSubject() == null) {
      throw new JwtException("No subject");
    }

    if (claims.getExpiration().before(new Date())) {
      throw new JwtException("Token expired");
    }
  }

  private String extractRoles(Claims claims) {
    Object roles = claims.get(ROLES_CLAIM);

    if (roles instanceof List<?>) {
      return ((List<?>) roles).stream()
          .filter(String.class::isInstance)
          .map(String.class::cast)
          .collect(Collectors.joining(","));
    }

    return "";
  }
}
