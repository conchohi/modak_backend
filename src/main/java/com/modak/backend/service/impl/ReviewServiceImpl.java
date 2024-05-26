package com.modak.backend.service.impl;

import com.modak.backend.dto.CampDto;
import com.modak.backend.dto.PageRequestDto;
import com.modak.backend.dto.ReviewDto;
import com.modak.backend.dto.response.PageResponseDto;
import com.modak.backend.entity.CampEntity;
import com.modak.backend.entity.ReviewEntity;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.repository.CampRepository;
import com.modak.backend.repository.ReviewRepository;
import com.modak.backend.repository.UserRepository;
import com.modak.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CampRepository campRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<ReviewDto> getList(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(
                pageRequestDto.getPage()-1, pageRequestDto.getSize()
        );
        Page<ReviewEntity> reviewPage = reviewRepository.getAllList(pageable);
        List<ReviewDto> dtoList = new ArrayList<>();
        for (ReviewEntity reviewEntity : reviewPage.getContent()) {
            ReviewDto reviewDto = entityToDto(reviewEntity);

            dtoList.add(reviewDto);
        }

        PageResponseDto<ReviewDto> pageResponseDto = PageResponseDto.<ReviewDto>builder()
                .pageRequestDTO(pageRequestDto)
                .dtoList(dtoList)
                .totalCount(reviewPage.getTotalElements())
                .build();

        return pageResponseDto;
    }

    @Override
    public List<ReviewDto> getListByUserId(String userId) {
        List<ReviewDto> dtoList = new ArrayList<>();
        List<ReviewEntity> ReviewList = reviewRepository.getReviewEntitiesByUserUserId(userId);
        for (ReviewEntity reviewEntity : ReviewList) {
            ReviewDto reviewDto = entityToDto(reviewEntity);
            dtoList.add(reviewDto);
        }
        return dtoList;
    }
    private ReviewDto entityToDto(ReviewEntity reviewEntity) {
        CampEntity campEntity = reviewEntity.getCamp();
        //캠핑장 이름과 번호, 이미지만 담기
        CampDto campDto = CampDto.builder()
                .campNo(campEntity.getCampNo())
                .name(campEntity.getName())
                .imgName(campEntity.getImgName())
                .build();

        ReviewDto reviewDto = ReviewDto.builder()
                .reviewNo(reviewEntity.getReviewNo())
                .campDto(campDto)
                .userNickname(reviewEntity.getUser().getNickname())
                .userProfileImage(reviewEntity.getUser().getProfileImage())
                .title(reviewEntity.getTitle())
                .content(reviewEntity.getContent())
                .score(reviewEntity.getScore())
                .createDate(reviewEntity.getCreateDate())
                .build();
        return reviewDto;
    }
    @Override
    @Transactional(readOnly = true)
    public ReviewDto getReview(Long reviewNo) {
        ReviewEntity reviewEntity = reviewRepository.getByReviewNo(reviewNo);
        if(reviewEntity == null){
            return null;
        }
        CampEntity campEntity = reviewEntity.getCamp();
        //캠핑장 이름과 번호, 이미지만 담기
        CampDto campDto = CampDto.builder()
                .campNo(campEntity.getCampNo())
                .name(campEntity.getName())
                .imgName(campEntity.getImgName())
                .build();

        ReviewDto reviewDto = ReviewDto.builder()
                .reviewNo(reviewEntity.getReviewNo())
                .campDto(campDto)
                .userNickname(reviewEntity.getUser().getNickname())
                .title(reviewEntity.getTitle())
                .content(reviewEntity.getContent())
                .score(reviewEntity.getScore())
                .createDate(reviewEntity.getCreateDate())
                .build();

        return reviewDto;
    }

    @Override
    public void delete(Long reviewNo, String id) {
        ReviewEntity reviewEntity = reviewRepository.getByReviewNo(reviewNo);
        CampEntity camp = reviewEntity.getCamp();
        //등록자의 아이디와 삭제 요청한 자의 아이디가 다르면
        if(!id.equals(reviewEntity.getUser().getUserId())){
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
        reviewRepository.deleteById(reviewNo);
    }

    @Override
    public Long register(ReviewDto reviewDto) {
        CampEntity camp = campRepository.getByCampNo(reviewDto.getCampNo());
        UserEntity user = userRepository.findByUserId(reviewDto.getUserId());

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .title(reviewDto.getTitle())
                .content(reviewDto.getContent())
                .score(reviewDto.getScore())
                .user(user)
                .camp(camp)
                .build();

        ReviewEntity result = reviewRepository.save(reviewEntity);

        return result.getReviewNo();
    }

    @Override
    public void modify(ReviewDto reviewDto) {
        ReviewEntity reviewEntity = reviewRepository.getByReviewNo(reviewDto.getReviewNo());

        if(!reviewDto.getUserId().equals(reviewEntity.getUser().getUserId())){
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        reviewEntity.change(
                reviewDto.getTitle(),
                reviewDto.getContent(),
                reviewDto.getScore()
        );
    }

    @Override
    public boolean isExistByUserIdAndCampNo(ReviewDto reviewDto) {
        String userId = reviewDto.getUserId();
        Long campNo = reviewDto.getCampNo();
        return reviewRepository.existsByCampCampNoAndUserUserId(campNo,userId);
    }
}
