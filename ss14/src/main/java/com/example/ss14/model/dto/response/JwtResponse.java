package com.example.ss14.model.dto.response;

import com.example.ss14.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {
    private final String type = "Bearer Token";
    private String accessToken;
    private User account;
}
