package com.marketplace.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marketplace.auth.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
  Optional<Token> findByRefreshToken(String refreshToken);

  void deleteByRefreshToken(String refreshToken);
}
