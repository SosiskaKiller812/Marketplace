package com.marketplace.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.user.entity.dto.UserDto;
import com.marketplace.user.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class UserController {
  private final UserService userService;

  @GetMapping
  public ResponseEntity<UserDto> getMethodName(@PathVariable Long id) {
    return ResponseEntity.ok().body(userService.findById(id));
  }

}
