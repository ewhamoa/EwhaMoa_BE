package com.example.EwhaMoa_BE;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="bookmark")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private int isClub;
    @Column(nullable = false)
    private Long postId;
}
