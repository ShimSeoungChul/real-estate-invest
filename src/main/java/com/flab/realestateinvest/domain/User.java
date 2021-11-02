package com.flab.realestateinvest.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class User {
    int id;
    String email;
    String password;
    String sessionId;
}
