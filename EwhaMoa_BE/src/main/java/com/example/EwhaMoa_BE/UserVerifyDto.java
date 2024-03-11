package com.example.EwhaMoa_BE;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVerifyDto {
    private String email;
    private int code;
}
