package com.example.EwhaMoa_BE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClubUpdateDto {
    private String groupName;
    private String title;
    private String body;
    private String due;
    private Integer affiliationType;
    private String affiliationName;
    private Integer topic;
    private Integer grade;
    private String imageLink;
}
