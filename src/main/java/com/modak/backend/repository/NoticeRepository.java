package com.modak.backend.repository;

import com.modak.backend.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    public List<NoticeEntity> findByNoticeNo(Long noticeNo);

}
