package com.marketplace.gateway.filter;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.marketplace.gateway.parser.RSAKeyParser;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class JwtAuthFilter implements GlobalFilter {

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

    // public endpoints
    if (path.startsWith("/api/auth") || path.startsWith("/fallback")) {
      return chain.filter(exchange);
    }

    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    String token = authHeader.substring(7);

    try {
      Jwts.parser()
          .verifyWith(publicKey)
          .requireIssuer("auth-service")
          .build()
          .parseSignedClaims(token);

      // Добавляем пользовательские данные в заголовки
      ServerHttpRequest request = exchange.getRequest().mutate()
          .header("X-User-Id", jwt.getSubject())
          .header("X-User-Roles", jwt.getClaimAsString("roles"))
          .build();

      ServletHttp
      return chain.filter(exchange.mutate().request(request).build());
    } catch (JwtException e) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }

}
