package com.flab.realestateinvest.controller;

import com.flab.realestateinvest.dto.SuccessResponseDto;
import com.flab.realestateinvest.dto.UserDto;
import com.flab.realestateinvest.interceptor.HttpSessionConfig;
import com.flab.realestateinvest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

@RestController
public class UserController {
    private UserService userService;
    private HttpSessionConfig httpSessionConfig;

    @Autowired
    public UserController(UserService userService, HttpSessionConfig httpSessionConfig){
        this.userService = userService;
        this.httpSessionConfig = httpSessionConfig;
    }

    @PostMapping("/users")
    public SuccessResponseDto<String> create(@RequestBody UserDto resource) throws NoSuchAlgorithmException {
        String email = resource.getEmail();
        String password = resource.getPassword();
        userService.registerUser(email, password);
        return new SuccessResponseDto<>("회원가입에 성공했습니다.","");
    }

    @PostMapping("/login")
    public SuccessResponseDto<String> login(@RequestBody UserDto resource, HttpSession session) throws NoSuchAlgorithmException {
        String email = resource.getEmail();
        String password = resource.getPassword();
        String sessionId = session.getId();
        userService.login(email,password,sessionId);
        duplicateSessionCheck(email);
        session.setAttribute(sessionId, email); // 세션 저장
        return new SuccessResponseDto<>("로그인에 성공했습니다.","");
    }

    // 로그인 한 아이디에 대한 다른 세션(다른 브라우저)이 유지되고 있는지 확인
    private void duplicateSessionCheck(String userId){
        for(HttpSession hs: httpSessionConfig.getActiveSessions()){
            String sid = hs.getId();
            String check = (String) hs.getAttribute(sid);
            // 발견되면 해당 세션 만료
            if(userId.equals(check)){
                hs.removeAttribute(sid);
            }
        }
    }

}
