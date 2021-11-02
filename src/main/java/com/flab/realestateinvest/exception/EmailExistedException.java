package com.flab.realestateinvest.exception;

public class EmailExistedException extends RuntimeException{
    public EmailExistedException(String email){
        super(email+ " 이미 존재하는 이메일입니다.");
    }
}
