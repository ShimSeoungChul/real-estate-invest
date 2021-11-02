package com.flab.realestateinvest.controller;

import com.flab.realestateinvest.domain.User;
import com.flab.realestateinvest.exception.EmailExistedException;
import com.flab.realestateinvest.exception.EmailNotExistedException;
import com.flab.realestateinvest.exception.PasswordWrongException;
import com.flab.realestateinvest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User resource){
        String email = resource.getEmail();
        String password = resource.getPassword();

        int userId;
        ResponseEntity responseEntity = null;
        try{
            userId = userService.registerUser(email, password);
            String url = "/users" + userId;
            responseEntity = ResponseEntity.created(new URI(url)).body("{}");
        }catch (EmailExistedException e){
            responseEntity = ResponseEntity.badRequest().body(e);
        }catch (Exception e){
            responseEntity = ResponseEntity.internalServerError().body(e);
        }

        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User resource, HttpSession httpSession){
        String email = resource.getEmail();
        String password = resource.getPassword();
        String sessionId = httpSession.getId();

        ResponseEntity responseEntity = null;
        try{
            User user = userService.login(email,password,sessionId);
            String url = "/login";
            responseEntity = ResponseEntity.created(new URI(url)).body(user);
        }catch (EmailNotExistedException | PasswordWrongException e ){
            responseEntity = ResponseEntity.badRequest().body(e);
        }catch (Exception e){
            responseEntity = ResponseEntity.internalServerError().body(e);
        }

        return responseEntity;
    }



}
