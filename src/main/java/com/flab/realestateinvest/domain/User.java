package com.flab.realestateinvest.domain;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class User {
    int id;
    String email;
    String password;
    String session_id;
}
