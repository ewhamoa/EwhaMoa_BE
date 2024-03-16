package com.example.EwhaMoa_BE;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ClubService {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

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
    public ClubDto getClub(Long postId, Long userId) {
        // 1. 엔티티 불러오기
        Club club = clubRepository.findById(postId).orElse(null);
        // 2. DTO로 변경
        Long checkBookmark = bookmarkRepository.existsByUserAndPostAndIsClub(userId, postId, 1);
        boolean isBookmarked = (checkBookmark == 1);
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
                club.getImageLink(),
                isBookmarked
        );
        return response;
    }

    public Club createPost(PostDto postDto, Long userId) {
        // 1. 현재 시간 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        String createdAt = sdf.format(currentTime);
        // 2. 소속 분류 설정
        Integer affiliationId = null;
        if (postDto.getAffiliation_type()==1) affiliationId = departmentRepository.findIdByName(postDto.getAffiliation_name());
        else if (postDto.getAffiliation_type()==2) affiliationId = collegeRepository.findIdByName(postDto.getAffiliation_name());
        // 3. 엔티티 생성
        Club club = new Club(
                null,
                userId,
                postDto.getGroupName(),
                postDto.getTitle(),
                postDto.getBody(),
                createdAt,
                postDto.getDue(),
                postDto.getAffiliation_type(),
                affiliationId,
                postDto.getTopic(),
                postDto.getGrade(),
                postDto.getImageLink()
        );
        clubRepository.save(club);
        return club;
    }

    public Club updatePost(Long postId, ClubUpdateDto dto) {
        Club target = clubRepository.findById(postId).orElse(null);
        if (target == null) return null;

        target.setGroupName(dto.getGroupName());
        target.setTitle(dto.getTitle());
        target.setBody(dto.getBody());
        target.setDue(dto.getDue());
        target.setAffiliationType(dto.getAffiliationType());
        Integer affiliationId = null;
        if (dto.getAffiliationType() == 1) affiliationId = departmentRepository.findIdByName(dto.getAffiliationName());
        if (dto.getAffiliationType() == 2) affiliationId = collegeRepository.findIdByName(dto.getAffiliationName());
        target.setAffiliationId(affiliationId);
        target.setTopic(dto.getTopic());
        target.setGrade(dto.getGrade());
        target.setImageLink(dto.getImageLink());

        Club updated = clubRepository.save(target);
        return updated;
    }

    public Club deletePost(Long postId) {
        Club target = clubRepository.findById(postId).orElse(null);
        if (target == null) return null;
        clubRepository.delete(target);
        return target;
    }
}
