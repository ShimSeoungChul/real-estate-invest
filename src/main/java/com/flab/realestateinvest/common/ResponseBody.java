package com.flab.realestateinvest.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ResponseBody {
    private String message;
    private Object result;

    public ResponseBody(String message){
        this.message = message;
    }

    public ResponseBody(String message, Object result){
        this.message = message;
        this.result = result;
    }
}
