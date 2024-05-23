package com.modak.backend.repository;

import com.modak.backend.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Integer> {
    List<FavoriteEntity> findByUserUserId(String userId);
    FavoriteEntity findByUserUserIdAndCampCampNo(String userId, Long campNo);
}
