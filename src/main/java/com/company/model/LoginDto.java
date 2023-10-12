package com.company.model;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    private boolean rememberMe;
}
