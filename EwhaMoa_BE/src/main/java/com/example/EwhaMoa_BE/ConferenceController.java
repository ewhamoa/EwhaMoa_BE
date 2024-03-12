package com.example.EwhaMoa_BE;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConferenceController {
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private ConferenceRepository conferenceRepository;

    // 전체 학회 홍보글 조회 API
    @GetMapping("/main/conference")
    public ResponseEntity<?> getConferences() {
        // 1. 필요한 DTO 가져오기
        List<ConferencesDto> responses = conferenceService.getConferences();
        // 2. 응답 처리
        return (responses != null)?
                ResponseEntity.status(HttpStatus.OK).body(responses):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 불러오기에 실패했습니다.");
    }

    // 특정 학회 홍보글 조회 API
    @GetMapping("/main/conference/{postId}")
    public ResponseEntity<?> getConference(@PathVariable Long postId) {
        // 1. 필요한 DTO 가져오기
        ConferenceDto response = conferenceService.getConference(postId);
        // 2. 응답 처리
        return (response != null)?
                ResponseEntity.status(HttpStatus.OK).body(response):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 불러오기에 실패했습니다.");

    }
}
