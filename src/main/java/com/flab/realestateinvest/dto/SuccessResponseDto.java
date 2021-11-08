package com.flab.realestateinvest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponseDto<T> {
    private String message;
    private T result;
}
