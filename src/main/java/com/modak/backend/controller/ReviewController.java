package com.modak.backend.controller;

import com.modak.backend.dto.PageRequestDto;
import com.modak.backend.dto.ReviewDto;
import com.modak.backend.dto.response.PageResponseDto;
import com.modak.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/{reviewNo}")
    public ResponseEntity<?> getReview(@PathVariable(name = "reviewNo") Long reviewNo){
        ReviewDto reviewDto = reviewService.getReview(reviewNo);
        if(reviewDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message","해당 리뷰가 존재하지 않습니다."));
        }
        return ResponseEntity.ok(Map.of("data",reviewDto));
    }
    @GetMapping("/list")
    public ResponseEntity<?> getReviewList(PageRequestDto pageRequestDto){
        PageResponseDto<ReviewDto> pageResponseDto = reviewService.getList(pageRequestDto);
        return ResponseEntity.ok(Map.of("data",pageResponseDto));
    }

    @PostMapping("")
    public ResponseEntity<?> register(@RequestBody ReviewDto reviewDto){
        //아이디 가져와서 등록
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        reviewDto.setUserId(id);
        //캠핑장 아이디, 후기 제목, 내용, 평점 보내야 함
        Long reviewNo = reviewService.register(reviewDto);
        return ResponseEntity.ok(Map.of("reviewNo",reviewNo));
    }

    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<?> delete(@PathVariable(name = "reviewNo") Long reviewNo){
        //아이디 가져옴
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        try{
            reviewService.delete(reviewNo, id);
        } catch (Exception e){
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message",e.getMessage()));
        }
        return ResponseEntity.ok(Map.of("message","success"));
    }

    @PatchMapping("/{reviewNo}")
    public ResponseEntity<?> modify(@PathVariable(name = "reviewNo") Long reviewNo,
                                    @RequestBody ReviewDto reviewDto){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        reviewDto.setUserId(id);
        reviewDto.setReviewNo(reviewNo);

        try{
            reviewService.modify(reviewDto);
        } catch (Exception e){
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message",e.getMessage()));
        }

        return ResponseEntity.ok(Map.of("message","success"));
    }

    @GetMapping("/listByUser")
    public ResponseEntity<?> getReviewListByUser(){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ReviewDto> dtoList = reviewService.getListByUserId(id);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/exist")
    public ResponseEntity<?> isExistByCampNo(@RequestParam("campNo") Long campNo){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setUserId(id);
        reviewDto.setCampNo(campNo);
        boolean isExist = reviewService.isExistByUserIdAndCampNo(reviewDto);
        return ResponseEntity.ok(Map.of("isExist",isExist));
    }
}
