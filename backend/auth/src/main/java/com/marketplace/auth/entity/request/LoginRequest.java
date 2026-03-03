package com.marketplace.auth.entity.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 4, max = 50, message = "Username should be between 4 and 50 symbols")
  private String username;

  @Column
  private String password;
}
