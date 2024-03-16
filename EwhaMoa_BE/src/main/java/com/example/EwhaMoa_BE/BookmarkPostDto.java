package com.example.EwhaMoa_BE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class BookmarkPostDto {
    private String groupName;
    private boolean isClub;
    private Long postId;
    private String title;
    private String imageLink;
}
