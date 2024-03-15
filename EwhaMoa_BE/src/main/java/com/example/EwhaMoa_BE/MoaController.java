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

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class MoaController {
    @Autowired
    private MoaService moaService;
    @Autowired
    private HttpSession session;

    @PostMapping("/chat/send")
    public ResponseEntity<?> chat(@RequestBody ChatDto chatDto, HttpServletRequest request) throws IOException {
        // 1. 메시지 분류
        Integer messageType = chatDto.getMessageType();
        String message = chatDto.getMessage();
        // 2. 서비스로 작업 넘기기
        if (messageType == 0) {
            ArrayList<?> responses = moaService.recommend(message);
            return (responses != null)?
                    ResponseEntity.status(HttpStatus.OK).body(responses):
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("답변 생성에 실패했습니다.");
        }
        else {
            session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            moaService.inquire(userId, message);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
}
