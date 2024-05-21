package com.modak.backend.repository;

import com.modak.backend.entity.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity, String>{
    CertificationEntity findByUserId(String userId);

    @Transactional
    void deleteByUserId(String userId);
}
