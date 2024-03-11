package com.example.EwhaMoa_BE;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String nickname;
    private String email;
    private String password;

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User toEntity() {
        return new User(null, this.nickname, this.email, this.password);
    }
}
