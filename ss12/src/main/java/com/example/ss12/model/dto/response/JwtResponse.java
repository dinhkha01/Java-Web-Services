package com.example.ss12.model.dto.response;

import com.example.ss12.model.entity.Account;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {
    private final String type = "Bearer Token";
    private String accessToken;
    private Account account;
}
