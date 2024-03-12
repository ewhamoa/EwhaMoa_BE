package com.example.EwhaMoa_BE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query(value = """
            SELECT
                CASE
                    WHEN pc.affiliation_type = 1 THEN d.name
                    WHEN pc.affiliation_type = 2 THEN c.name
                END AS affiliation_name
            FROM
                post_club pc
            LEFT JOIN department d ON pc.affiliation_id = d.department_id AND pc.affiliation_type = 1
            LEFT JOIN college c ON pc.affiliation_id = c.college_id AND pc.affiliation_type = 2
            WHERE post_id = :postId""", nativeQuery = true)
    String findAffiliationName(@Param("postId") Long postId);
}
