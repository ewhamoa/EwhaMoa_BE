package com.example.EwhaMoa_BE;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ConferenceRepository conferenceRepository;

    public int changeBookmark(Long userId, int isClub, Long postId) {
        Integer isCreated = 1;
        // 1. 북마크 존재 여부 확인
        if (bookmarkRepository.existsByUserAndPostAndIsClub(userId, postId, isClub) == 1) {
            // 2. 삭제 처리
            isCreated = 0;
            bookmarkRepository.delete(userId, postId, isClub);
        }
        else {
            // 3. 생성 처리
            Bookmark bookmark = new Bookmark(null, userId, isClub, postId);
            bookmarkRepository.save(bookmark);
        }
        return isCreated;
    }

    public ArrayList<BookmarkPostDto> showBookmarks(Long userId) {
        // 1. 엔티티 불러오기
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(userId);
        // 2. DTO로 변경
        ArrayList<BookmarkPostDto> responses = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            BookmarkPostDto bookmarkPostDto;
            if (bookmark.getIsClub() == 1) {
                bookmarkPostDto = new BookmarkPostDto(
                        clubRepository.findGroupNameByPostId(bookmark.getPostId()),
                        true,
                        bookmark.getPostId(),
                        clubRepository.findTitleByPostId(bookmark.getPostId())
                );
            }
            else {
                bookmarkPostDto = new BookmarkPostDto(
                        conferenceRepository.findGroupNameByPostId(bookmark.getPostId()),
                        false,
                        bookmark.getPostId(),
                        conferenceRepository.findTitleByPostId(bookmark.getPostId())
                );
            }
            responses.add(bookmarkPostDto);
        }
        return responses;
    }
}
