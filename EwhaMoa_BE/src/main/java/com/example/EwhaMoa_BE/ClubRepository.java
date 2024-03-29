package com.example.EwhaMoa_BE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM post_club WHERE group_name = :recommendedClubName", nativeQuery = true)
    Long existsByName(@Param("recommendedClubName") String recommendedClubName);

    @Query(value = "SELECT post_id FROM post_club WHERE group_name = :groupName ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Long findLatestPostIdByGroupName(@Param("groupName") String groupName);

    @Query(value = "SELECT title FROM post_club WHERE post_id = :postId", nativeQuery = true)
    String findTitleByPostId(@Param("postId") Long postId);

    @Query(value = "SELECT group_name FROM post_club WHERE post_id = :postId", nativeQuery = true)
    String findGroupNameByPostId(@Param("postId") Long postId);

    @Query(value = "SELECT image_link FROM post_club WHERE post_id = :postId", nativeQuery = true)
    String findImageLink(@Param("postId") Long postId);

    @Query(value = "SELECT * FROM post_club WHERE user_id = :userId", nativeQuery = true)
    List<Club> findAllByUser(@Param("userId") Long userId);

    @Query(value = "SELECT user_id FROM post_club WHERE post_id = :postId", nativeQuery = true)
    Long findUserIdByPostId(@Param("postId") Long postId);
}
