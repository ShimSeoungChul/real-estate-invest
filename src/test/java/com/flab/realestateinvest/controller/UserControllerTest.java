package com.flab.realestateinvest.controller;

import com.flab.realestateinvest.service.UserService;
import com.flab.realestateinvest.domain.User;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
     MockMvc mvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    public void signUp() throws Exception{
        String email = "abcd@naver.com";
        String password = "123456789aA!";

        User user = User.builder()
                .email(email)
                .password(password)
                .build();

        given(userService.registerUser(email, password))
                .willReturn(1);

        Gson gson = new Gson();
        String content = gson.toJson(user);

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());

        verify(userService).registerUser(email, password);
    }

    @Test
    public void login() throws Exception{
        String email = "abcd@naver.com";
        String encodedPassword = "$2a$10$zAsi1kwbIM02D81X1SdBsOIFhMBR559TtH.4FMMqz4Z0ztwrYxiLi";
        String sessionId = "1";

        User user = User.builder()
                .email(email)
                .sessionId(sessionId)
                .build();

        given(userService.login(email, encodedPassword,sessionId))
                .willReturn(user);

        Gson gson = new Gson();
        String content = gson.toJson(user);

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());
    }
}