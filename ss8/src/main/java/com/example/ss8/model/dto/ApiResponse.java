package com.example.ss8.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiResponse <T>{
    private Boolean status;
    private String message;
    private T data;
}
