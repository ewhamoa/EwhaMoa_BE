package com.example.EwhaMoa_BE;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    @Autowired
    private ClubService clubService;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private HttpSession session;
    // 통합 게시글 업로드 API
    @PostMapping("/main/post")
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto, HttpServletRequest request) {
        // 1. userId 받아오기
        session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        // 2. 게시글 생성
        if (postDto.isClub()) {
            Club club = clubService.createPost(postDto, userId);
            return (club != null)?
                    ResponseEntity.status(HttpStatus.OK).build():
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시글 생성에 실패했습니다.");
        }
        else {
            Conference conference = conferenceService.createPost(postDto, userId);
            return (conference != null)?
                    ResponseEntity.status(HttpStatus.OK).build():
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시글 생성에 실패했습니다.");
        }
    }

}
