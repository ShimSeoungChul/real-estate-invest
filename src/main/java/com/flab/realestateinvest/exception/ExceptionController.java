package com.flab.realestateinvest.exception;

import com.flab.realestateinvest.dto.FailResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.NoSuchAlgorithmException;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailNotExistedException.class)
    public FailResponseDto handleEmailNotExistedException(EmailNotExistedException e) {
        return new FailResponseDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public FailResponseDto handleNoSuchAlgorithmException(NoSuchAlgorithmException e) {
        return new FailResponseDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordWrongException.class)
    public FailResponseDto handlePasswordWrongException(PasswordWrongException e) {
        return new FailResponseDto(e.getMessage());
    }
}
