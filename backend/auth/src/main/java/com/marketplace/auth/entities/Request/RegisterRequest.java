package com.marketplace.auth.entities.Request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    private String name;

    @Email
    private String email;

    private String password;
}
