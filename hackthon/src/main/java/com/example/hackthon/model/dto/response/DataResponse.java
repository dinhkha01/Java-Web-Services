package com.example.hackthon.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DataResponse<T> {
    private int code;
    private String key;
    private T data;
}