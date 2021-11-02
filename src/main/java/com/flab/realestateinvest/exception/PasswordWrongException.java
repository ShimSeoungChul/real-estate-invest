package com.flab.realestateinvest.exception;

public class PasswordWrongException extends RuntimeException{
    public PasswordWrongException(){
        super("잘못된 비밀번호입니다.");
    }
}
