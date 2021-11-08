package com.flab.realestateinvest.domain;

import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
public class User {
    private int id;
    private String email;
    private String password;
    private String sessionId;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
