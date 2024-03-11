package com.example.EwhaMoa_BE;

import com.univcert.api.UnivCert;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;

    // 체크용 API
    @GetMapping("/check")
    public ResponseEntity<?> check() {
        return ResponseEntity.status(HttpStatus.OK).body("hi");
    }

    // 이메일 발송 요청 API
    @PostMapping("/signup/check")
    public ResponseEntity<?> sendEmail(@RequestBody UserEmailDto userEmailDto) throws IOException {
        // 1. 이메일 발송 request
        Map<String, Object> response = UnivCert.certify("e2cbf6e7-d65c-40d3-b528-9e3d67b65f83", userEmailDto.getEmail(), "이화여자대학교", true);
        // 2. response 확인
        Boolean value = (Boolean) response.get("success");
        return (value)?
                ResponseEntity.status(HttpStatus.OK).build() : // 성공시 200
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.get("message")); // 실패시 400, 오류 메시지
    }

    // 코드 확인 API
    @PostMapping("/signup/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody UserVerifyDto userVerifyDto) throws IOException {
        // 1. 코드 확인 request
        Map<String, Object> response = UnivCert.certifyCode("e2cbf6e7-d65c-40d3-b528-9e3d67b65f83", userVerifyDto.getEmail(), "이화여자대학교", userVerifyDto.getCode());
        // 2. response 확인
        Boolean value = (Boolean) response.get("success");
        return (value)?
                ResponseEntity.status(HttpStatus.OK).build(): // 성공시 200
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.get("message")); // 실패시 400, 오류 메시지
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) throws Exception {
        // 1. 이메일 중복 확인
        if (userService.checkVerifiedEmail(userLoginDto.getEmail()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
        // 2. 유저 엔티티 생성
        User user = userService.signup(userLoginDto); // 닉네임까지 여기서 만들기
        // 2. 생성 결과 구분
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 혹은 비밀번호가 입력되지 않았습니다.");
        session = request.getSession();
        session.setAttribute("userId", user.getUserId()); // 세션에 ID 주기
        UserSignedDto userSignedDto = new UserSignedDto(user.getNickname(), user.getUserId()); // response용 Dto 생성
        return ResponseEntity.status(HttpStatus.OK).header(session.getId()).body(userSignedDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) throws NoSuchAlgorithmException {
        User user = userService.login(userLoginDto);
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 존재하지 않거나 비밀번호가 일치하지 않습니다.");
        session = request.getSession();
        session.setAttribute("userId", user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).header(session.getId()).body(user.getUserId());
    }
}
