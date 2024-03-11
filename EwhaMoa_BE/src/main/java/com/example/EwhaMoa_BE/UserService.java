package com.example.EwhaMoa_BE;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 회원가입
    public User signup(UserLoginDto userLoginDto) throws NoSuchAlgorithmException {
        // 1. 닉네임, 비밀번호 set
        UserDto userDto = new UserDto(userLoginDto.getEmail(), userLoginDto.getPassword());
        userDto.setNickname(generate());
        userDto.setPassword(encrypt(userDto.getPassword()));
        // 2. DTO -> Entity
        User user = userDto.toEntity();
        // 3. 저장
        return userRepository.save(user);
    }

    // 이메일 중복 확인
    public boolean checkVerifiedEmail(String verifiedEmail) {
        User user = userRepository.findByEmail(verifiedEmail).orElse(null);
        return (user != null);
    }

    // 로그인
    public User login(UserLoginDto userLoginDto) throws NoSuchAlgorithmException {
        // 1. email, password 불러오기
        User user = userRepository.findByEmail(userLoginDto.getEmail()).orElse(null);
        String encodingPassword = encrypt(userLoginDto.getPassword());
        // 2. 검사
        if(user != null && (user.getPassword().equals(encodingPassword)))
            return user;
        else
            return null;
    }

    // 닉네임 생성
    public String generate() {
        String adjective = "임시";
        String noun = "닉네임";
        return adjective + noun;
    }

    // 비밀번호 암호화
    public static String encrypt(String password) throws NoSuchAlgorithmException {
        // 1. 데이터 세팅
        MessageDigest md = MessageDigest.getInstance("SHA-256"); // 알고리즘 세팅
        byte[] passByte = password.getBytes(StandardCharsets.UTF_8); // 바이트 배열로 변환
        md.reset();
        // 2. 암호화
        byte[] digested = md.digest(passByte);
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < digested.length; i++)
            stringBuffer.append(Integer.toHexString(0xff & digested[i]));
        // 3. 해시값 리턴
        return stringBuffer.toString();
    }
}
