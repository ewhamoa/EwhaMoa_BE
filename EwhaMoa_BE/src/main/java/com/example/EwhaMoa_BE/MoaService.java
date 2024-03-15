package com.example.EwhaMoa_BE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoaService {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ConferenceRepository conferenceRepository;
    @Autowired
    private InquireRepository inquireRepository;

    public ArrayList<?> recommend(String clubName) {
        ArrayList <RecommendationDto> responses = new ArrayList<>();
        ArrayList <String> errors = new ArrayList<>();
        // 1. 결과 가져오기
        String responseText = "";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "/home/ec2-user/EwhaMoa_BE/EwhaMoa_BE/src/main/python/RecommendClub.py", clubName);
            Process process = processBuilder.start();

            // 1. 오류 메시지 출력
            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));

            String errorMessage = "";
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorMessage += errorLine;
            }

            // 2. 결과 출력
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseText += line;
            }

            // 3. 종료 처리
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                errors.add(errorMessage + "\n" + Integer.toString(exitCode));
                return errors;
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }

        // 2. 반환
        Boolean isClub;
        Long postId;
        String title;
        String[] recommendedClubs = responseText.substring(responseText.indexOf("(") + 1, responseText.lastIndexOf(")")).split(",\\s*");
        for (int i = 0; i < recommendedClubs.length; i++) {
            recommendedClubs[i] = recommendedClubs[i].replaceAll("'", "");
        }
        for (String recommendedClubName : recommendedClubs) {
            // 1. isClub
            if (clubRepository.existsByName(recommendedClubName) == 1) isClub = true;
            else isClub = false;
            // 2. postId
            if (isClub) {
                postId = clubRepository.findLatestPostIdByGroupName(recommendedClubName);
                title = clubRepository.findTitleByPostId(postId);
            }
            else {
                postId = conferenceRepository.findLatestPostIdByGroupName(recommendedClubName);
                title = conferenceRepository.findTitleByPostId(postId);
            }
            // 4. DTO 만들기
            RecommendationDto recommendationDto = new RecommendationDto(recommendedClubName, isClub, postId, title);
            responses.add(recommendationDto);
        }

        return responses;
    }

    public void inquire(Long userId, String message) {
        Inquire inquire = new Inquire(null, userId, message);
        inquireRepository.save(inquire);
    }
}
