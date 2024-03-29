package com.example.EwhaMoa_BE;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ConferenceRepository conferenceRepository;

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
        Random random = new Random();
        String[] adjectives = {"기획하는","개발하는","디자인하는","발표하는","잉계에서자는","공대까지걸어가는","학관에서길잃은","조예대계단에게패배한","대강당으로달려가는","교육관에서팀플하는","아샷추마시는", "과제하는",
                "총장쿠키받은",
                "4온스학잠입은",
                "2온스학잠입은",
                "코딩공부하는",
                "기숙사엘베탑에당도한",
                "연협에서떡볶이먹는",
                "포관에서밤샘하는",
                "채플개근한",
                "채플에서자는",
                "통학하는"};
        String[] nouns = {"혜준이","예송이","연수","나은이","겨레","배꽃","화연","이대생","벗","뽀미","뽀삐","이화곰돌이","용용벗","음메벗","찍찍벗","토깽벗","호랭벗", "이데생",
                "화여니"};
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
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

    public ProfileDto showMyProfile(Long userId) {
        // 1. 엔티티 불러오기
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        // 2. DTO 생성
        ProfileDto response = new ProfileDto(
                user.getNickname(),
                user.getEmail()
        );
        return response;
    }

    public ArrayList<MyPostDto> showMyPosts(Long userId) {
        ArrayList<MyPostDto> responses = new ArrayList<>();
        // 1. 동아리 추가
        List<Club> clubs = clubRepository.findAllByUser(userId); // 엔티티 불러오기
        for (Club club : clubs) {
            MyPostDto myPostDto = new MyPostDto(
                    true,
                    club.getPostId(),
                    club.getTitle(),
                    club.getBody(),
                    club.getCreatedAt(),
                    club.getDue(),
                    club.getAffiliationType(),
                    clubRepository.findAffiliationName(club.getPostId()),
                    club.getTopic(),
                    club.getGrade(),
                    club.getImageLink()
            );
            responses.add(myPostDto);
        }
        // 2. 학회 추가
        List<Conference> conferences = conferenceRepository.findAllByUser(userId); // 엔티티 불러오기
        for (Conference conference : conferences) {
            MyPostDto myPostDto = new MyPostDto(
                    false,
                    conference.getPostId(),
                    conference.getTitle(),
                    conference.getBody(),
                    conference.getCreatedAt(),
                    conference.getDue(),
                    conference.getAffiliationType(),
                    conferenceRepository.findAffiliationName(conference.getPostId()),
                    conference.getTopic(),
                    conference.getGrade(),
                    conference.getImageLink()
            );
            responses.add(myPostDto);
        }
        return responses;
    }
}
