package com.flab.realestateinvest.service;

import java.security.NoSuchAlgorithmException;

import com.flab.realestateinvest.domain.User;
import com.flab.realestateinvest.exception.EmailExistedException;
import com.flab.realestateinvest.exception.EmailNotExistedException;
import com.flab.realestateinvest.exception.PasswordWrongException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void login() throws Exception{
        String email = "abcd@naver.com";
        String password = "123456789aA!";
        String sessionId = "";

        // 회원가입
        userService.registerUser(email, password);

        //로그인
        User user = userService.login(email,password,sessionId);

        assertThat(user.getEmail())
                .isEqualTo(email);
        assertThat(user.getSessionId())
                .isEqualTo(sessionId);
        assertThat(user.getPassword())
                .isNull();
    }

    @Test
    public void loginWithNotExistedEmail() throws Exception{
        String email = "abcd@naver.com";
        String password = "123456789aA!";
        String sessionId = "1";

        assertThatThrownBy(()->{
            // 존재하지 않는 이메일로 로그인하면, 로그인하면
            userService.login(email, password, sessionId);
        }).isInstanceOf(PasswordWrongException.class) //예외 발생
                .hasMessageContaining("잘못된 비밀번호입니다.");
    }

    @Test
    public void loginWithWrongPassword() throws NoSuchAlgorithmException {
        String email = "abcd@naver.com";
        String password = "123456789aA!";

        // 회원가입
        userService.registerUser(email, password);

        assertThatThrownBy(()->{
            // 잘못된 비밀번호로 로그인하면,
            String wrongPassword = "wrong!";
            userService.login(email, wrongPassword, "session");
        }).isInstanceOf(EmailNotExistedException.class)//예외 발생
                .hasMessageContaining(email+"라는 이메일은 존재하지 않습니다.");

    }

    @Test
    public void registerUser() throws NoSuchAlgorithmException {
        String email = "abc@naver.com";
        String password = "123456789aA!";
        int id = userService.registerUser(email, password);

        assertThat(id)
                .isGreaterThanOrEqualTo(1);
    }

    @Test
    public void registerUserWithExistedException() throws NoSuchAlgorithmException{
        String email = "abc@naver.com";
        String password = "123456789aA!";

        assertThatThrownBy(() -> {
            //똑같은 정보로 두 번 회원가입을 시도하면
            userService.registerUser(email, password);
            userService.registerUser(email, password);
        }).isInstanceOf(EmailExistedException.class) //예외 발생
                .hasMessageContaining(email + " 이미 존재하는 이메일입니다.");
    }

    @Test
    public void getEncodedPassword() throws NoSuchAlgorithmException {
        String password = "123456789aA!";
        String encodedPassword = userService.getEncodedPassword(password);
        assertThat(encodedPassword)
                .isEqualTo("b943a93742723379c27cc72610fcb3dc9b235d1cf7f65848c5c4514e6377c80311133512aedc4f5d5818a719d6f0f24546a0007c33ccee4db8041177eeda6c4c");
    }

}