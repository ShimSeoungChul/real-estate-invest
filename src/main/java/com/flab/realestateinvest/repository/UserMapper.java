package com.flab.realestateinvest.repository;

import com.flab.realestateinvest.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    int save(User user);
    Optional<User> findByEmail(String email);
}
