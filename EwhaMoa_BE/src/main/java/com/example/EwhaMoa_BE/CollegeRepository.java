package com.example.EwhaMoa_BE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeRepository extends JpaRepository<College, Integer> {
    @Query(value = "SELECT college_id FROM college WHERE name = :affiliationName", nativeQuery = true)
    Integer findIdByName(@Param("affiliationName") String affiliationName);
}
