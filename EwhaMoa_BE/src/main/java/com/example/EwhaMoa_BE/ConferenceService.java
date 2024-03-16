package com.example.EwhaMoa_BE;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    // 전체 동아리 조회
    public List<ConferencesDto> getConferences() {
        // 1. 엔티티 불러오기
        List<Conference> conferences = conferenceRepository.findAll();
        // 2. DTO로 변경
        ArrayList<ConferencesDto> responses = new ArrayList<ConferencesDto>();
        for (Conference conference: conferences) {
            ConferencesDto conferencesDto = new ConferencesDto(
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
            responses.add(conferencesDto);
        }
        return responses;
    }

    public ConferenceDto getConference(Long postId, Long userId) {
        // 1. 엔티티 불러오기
        Conference conference = conferenceRepository.findById(postId).orElse(null);
        // 2. DTO로 변경
        Long checkBookmark = bookmarkRepository.existsByUserAndPostAndIsClub(userId, postId, 0);
        boolean isBookmarked = (checkBookmark == 1);
        ConferenceDto response = new ConferenceDto(
                conference.getPostId(),
                conference.getGroupName(),
                conference.getTitle(),
                conference.getBody(),
                conference.getCreatedAt(),
                conference.getDue(),
                conference.getAffiliationType(),
                conferenceRepository.findAffiliationName(conference.getPostId()),
                conference.getTopic(),
                conference.getGrade(),
                conference.getImageLink(),
                isBookmarked
        );
        return response;
    }

    public Conference createPost(PostDto postDto, Long userId) {
        // 1. 현재 시간 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        String createdAt = sdf.format(currentTime);
        // 2. 소속 분류 설정
        Integer affiliationId = null;
        if (postDto.getAffiliation_type()==1) affiliationId = departmentRepository.findIdByName(postDto.getAffiliation_name());
        else if (postDto.getAffiliation_type()==2) affiliationId = collegeRepository.findIdByName(postDto.getAffiliation_name());
        // 3. 엔티티 생성
        Conference conference = new Conference(
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
        conferenceRepository.save(conference);
        return conference;
    }
}
