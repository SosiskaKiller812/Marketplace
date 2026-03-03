package com.marketplace.auth.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.marketplace.auth.entity.User;
import com.marketplace.auth.entity.response.UserResponse;

@Component
public class UserMapper {

  public UserResponse toUserResponse(User user) {

    List<String> roles = user.getRoles().stream()
        .map(role -> role.toString())
        .toList();

    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        roles);
  }
}
