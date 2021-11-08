package com.flab.realestateinvest.service;

import com.flab.realestateinvest.domain.User;
import com.flab.realestateinvest.exception.EmailExistedException;
import com.flab.realestateinvest.exception.EmailNotExistedException;
import com.flab.realestateinvest.exception.PasswordWrongException;
import com.flab.realestateinvest.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.Optional;

@Service
public class UserService {
    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public User login(String email, String password, String sessionId)  {
        // 로그인을 원하는 이메일을 가진 고객 정보 가져오기
        User searched = userMapper.findByEmail(email)
                //고객 정보가 없다면 예외 발생
                .orElseThrow(() -> new EmailNotExistedException(email));
        String encryptedPassword = searched.getPassword();

        // 이메일에 해당하는 비밀번호가 매칭되는지 인증
        if(!BCrypt.checkpw(password, encryptedPassword)){
            throw new PasswordWrongException();
        }

        // 인증이 성공하면 세션아이디를 업데이트하고, 필요한 유저 정보 리턴
        searched.setSessionId(sessionId);
        userMapper.update(searched);

        return User.builder()
                .id(searched.getId())
                .email(searched.getEmail())
                .sessionId(searched.getSessionId())
                .build();
    }

    public int registerUser(String email, String password) {
        // 이미 중복된 아이디가 존재하면, 예외 발생
        Optional<User> searched = userMapper.findByEmail(email);

        if(searched.isPresent()){
            throw new EmailExistedException(email);
        }

        // 비밀번호를 암호화하여 저장
        String encrypted = getEncryptedPassword(password);
        User user = User.builder()
                .email(email)
                .password(encrypted)
                .build();
        return userMapper.save(user);
    }

    public String getEncryptedPassword(String password)  {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
