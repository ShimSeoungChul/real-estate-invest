package com.flab.realestateinvest.service;

import com.flab.realestateinvest.domain.User;
import com.flab.realestateinvest.exception.EmailExistedException;
import com.flab.realestateinvest.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {
    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public int registerUser(String email, String password) throws NoSuchAlgorithmException {
        // 이미 중복된 아이디가 존재하면,
        Optional<User> searched = userMapper.findByEmail(email);
        if(searched.isEmpty()){
            throw new EmailExistedException(email); //예외 발생
        }

        // 비밀번호를 암호화하여 저장
        String encodedPassword = getEncodedPassword(password);
        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();
        return userMapper.save(user);
    }

    public String getEncodedPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(password.getBytes());
        String encodedPassword = String.format("%0128x", new BigInteger(1, md.digest()));;
        return encodedPassword;
    }

}
