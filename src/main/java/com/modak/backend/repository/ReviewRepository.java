package com.modak.backend.repository;

import com.modak.backend.dto.ReviewDto;
import com.modak.backend.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("select r from ReviewEntity r " +
            "left join fetch r.camp " +
            "left join fetch r.user " +
            "order by r.reviewNo desc ")
    public Page<ReviewEntity> getAllList(Pageable pageable);

    @Query("select r from ReviewEntity r " +
            "left join fetch r.camp " +
            "left join fetch r.user " +
            "where r.reviewNo = :reviewNo")
    public ReviewEntity getByReviewNo(@Param("reviewNo") Long reviewNo);

    @Query("select r from ReviewEntity r left join fetch r.camp " +
            "where r.user.userId = :userId order by r.reviewNo desc ")
    public List<ReviewEntity> getReviewEntitiesByUserUserId(@Param("userId") String userId);

    public boolean existsByCampCampNoAndUserUserId(Long campNo, String userId);
}
