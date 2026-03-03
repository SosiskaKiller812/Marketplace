package com.marketplace.user.mapper.impl;

import org.springframework.stereotype.Component;

import com.marketplace.user.entity.User;
import com.marketplace.user.entity.dto.UserDto;
import com.marketplace.user.mapper.Mapper;

@Component
public class UserMapper implements Mapper<User, UserDto>{
  public User toEntity(UserDto userDto) {
    return User.builder()
        .id(userDto.id())
        .nickname(userDto.nickname())
        .description(userDto.description())
        .build();
  }

  public UserDto toDto(User user) {
    return new UserDto(
        user.getId(),
        user.getNickname(),
        user.getDescription());
  }
}
