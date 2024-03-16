package com.example.EwhaMoa_BE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="college")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class College {
    @Id
    private Integer collegeId;
    @Column
    private String name;
}
