package com.example.ss14.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DataResponse<T> {
    private int status;
    private String message;
    private T data;
}