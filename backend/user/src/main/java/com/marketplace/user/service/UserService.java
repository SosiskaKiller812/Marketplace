package com.marketplace.user.service;

import org.springframework.stereotype.Service;

import com.marketplace.user.entity.User;
import com.marketplace.user.entity.dto.UserDto;
import com.marketplace.user.mapper.impl.UserMapper;
import com.marketplace.user.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserDto findById(Long id) {
    User user = userRepository.findById(id).orElseThrow(

    );

    return userMapper.toDto(user);
  }
}
