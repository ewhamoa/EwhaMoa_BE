package com.example.EwhaMoa_BE;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="inquire")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Inquire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquireId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String inquire;
}
