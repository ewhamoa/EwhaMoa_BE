package com.example.EwhaMoa_BE;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    @Autowired
    private ClubRepository clubRepository;

    // 전체 동아리 조회
    public List<ClubsDto> getClubs() {
        // 1. 엔티티 불러오기
        List<Club> clubs = clubRepository.findAll();
        // 2. DTO로 변경
        ArrayList<ClubsDto> responses = new ArrayList<ClubsDto>();
        for (Club club: clubs) {
            ClubsDto clubsDto = new ClubsDto(
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
            responses.add(clubsDto);
        }
        return responses;
    }

    // 특정 동아리 조회
    public ClubDto getClub(Long postId) {
        // 1. 엔티티 불러오기
        Club club = clubRepository.findById(postId).orElse(null);
        // 2. DTO로 변경
        ClubDto response = new ClubDto(
                club.getPostId(),
                club.getGroupName(),
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
        return response;
    }
}
