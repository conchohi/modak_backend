package com.modak.backend.service;

import com.modak.backend.dto.PageRequestDto;
import com.modak.backend.dto.ReviewDto;
import com.modak.backend.dto.response.PageResponseDto;

import java.util.List;

public interface ReviewService {
    public PageResponseDto<ReviewDto> getList(PageRequestDto pageRequestDto);
    public List<ReviewDto> getListByUserId(String userId);
    public ReviewDto getReview(Long reviewNo);
    public void delete(Long reviewNo, String id);
    public Long register(ReviewDto reviewDto);
    public void modify(ReviewDto reviewDto);

    public boolean isExistByUserIdAndCampNo(ReviewDto reviewDto);
}
