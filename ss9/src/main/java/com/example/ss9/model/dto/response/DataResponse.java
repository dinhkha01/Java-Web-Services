package com.example.ss9.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DataResponse<T> {
    private String key;
    private T data;
}