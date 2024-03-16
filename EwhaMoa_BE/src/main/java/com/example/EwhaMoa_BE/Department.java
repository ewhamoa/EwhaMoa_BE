package com.example.EwhaMoa_BE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="department")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Department {
    @Id
    private Integer departmentId;
    @Column
    private String name;
}
