package com.example.EwhaMoa_BE;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(bookmark_id) > 0 THEN true ELSE false END FROM Bookmark WHERE user_id = :userId AND post_id = :postId AND is_club = :isClub", nativeQuery = true)
    Long existsByUserAndPostAndIsClub(@Param("userId") Long userId, @Param("postId") Long postId, @Param("isClub") int isClub);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Bookmark WHERE user_id = :userId AND post_id = :postId AND is_club = :isClub", nativeQuery = true)
    void delete(@Param("userId") Long userId, @Param("postId") Long postId, @Param("isClub") int isClub);

    @Query(value = "SELECT * FROM bookmark WHERE user_id = :userId", nativeQuery = true)
    List<Bookmark> findAllByUser(@Param("userId") Long userId);
}
