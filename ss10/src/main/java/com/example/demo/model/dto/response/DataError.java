package com.example.demo.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@Builder
public class DataError {
    private int code;
    private String message;
    private Map<String, String> details;
}