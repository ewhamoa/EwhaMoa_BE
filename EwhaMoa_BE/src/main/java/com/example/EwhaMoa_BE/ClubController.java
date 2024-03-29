package com.example.EwhaMoa_BE;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClubController {
    @Autowired
    private ClubService clubService;
    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private HttpSession session;

    // 전체 동아리 홍보글 조회 API
    @GetMapping("/main/club")
    public ResponseEntity<?> getClubs(HttpServletRequest request) {
        // 1. 필요한 DTO 가져오기
        session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        List<ClubsDto> responses = clubService.getClubs(userId);
        // 2. 응답 처리
        return (responses != null)?
                ResponseEntity.status(HttpStatus.OK).body(responses):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 불러오기에 실패했습니다.");
    }

    // 특정 동아리 홍보글 조회 API
    @GetMapping("/main/club/{postId}")
    public ResponseEntity<?> getClub(@PathVariable(name="postId") Long postId, HttpServletRequest request) {
        // 1. 필요한 DTO 가져오기
        session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        ClubDto response = clubService.getClub(postId, userId);
        // 2. 응답 처리
        return (response != null)?
                ResponseEntity.status(HttpStatus.OK).body(response):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 불러오기에 실패했습니다.");
    }

    // 북마크 API
    @PostMapping("/main/club/{postId}/bookmark")
    public ResponseEntity<?> changeClubBookmark(@PathVariable(name="postId") Long postId, HttpServletRequest request) {
        // 1. 엔티티 생성
        session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        int isClub = 1;
        Integer response = bookmarkService.changeBookmark(userId, isClub, postId);
        // 2. 응답 처리
        return (response != null)?
                ResponseEntity.status(HttpStatus.OK).build():
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("postId가 잘못되었습니다.");
    }

    // 게시글 수정 API
    @PatchMapping("/main/club/{postId}/update")
    public ResponseEntity<?> updatePost(@PathVariable(name="postId") Long postId, @RequestBody ClubUpdateDto clubUpdateDto) {
        Club response = clubService.updatePost(postId, clubUpdateDto);
        return (response != null)?
                ResponseEntity.status(HttpStatus.OK).build():
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정하려는 게시글이 없습니다.");
    }

    @DeleteMapping("/main/club/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable(name="postId") Long postId) {
        Club response = clubService.deletePost(postId);
        return (response != null)?
                ResponseEntity.status(HttpStatus.OK).build():
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제하려는 게시글이 없습니다.");
    }
}
