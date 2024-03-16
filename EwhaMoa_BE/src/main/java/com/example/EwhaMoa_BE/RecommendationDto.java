package com.example.EwhaMoa_BE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RecommendationDto {
    private String groupName;
    private Boolean isClub;
    private Long postId;
    private String title;
    private String imageLink;
}
