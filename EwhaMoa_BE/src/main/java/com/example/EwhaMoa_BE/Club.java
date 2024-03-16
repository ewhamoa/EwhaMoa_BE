package com.example.EwhaMoa_BE;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="post_club")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column
    private Long userId;
    @Column(nullable = false)
    private String groupName;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String body;
    @Column(nullable = false)
    private String createdAt;
    @Column(nullable = false)
    private String due;
    @Column(nullable = false)
    private Integer affiliationType;
    @Column
    private Integer affiliationId;
    @Column(nullable = false)
    private Integer topic;
    @Column(nullable = false)
    private Integer grade;
    @Column
    private String imageLink;
}
