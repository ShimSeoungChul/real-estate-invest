package com.flab.realestateinvest.exception;

public class EmailNotExistedException extends RuntimeException{

    public EmailNotExistedException(String email){
        super(email+"라는 이메일은 존재하지 않습니다.");
    }
}
