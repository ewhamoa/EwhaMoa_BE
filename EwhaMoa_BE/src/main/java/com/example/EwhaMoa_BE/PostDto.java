package com.example.EwhaMoa_BE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private boolean isClub;
    private String groupName;
    private String title;
    private String body;
    private String due;
    private int affiliation_type;
    private String affiliation_name;
    private int topic;
    private int grade;
    private String imageLink;
}
