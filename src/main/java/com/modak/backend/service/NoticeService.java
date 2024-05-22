package com.modak.backend.service;

import com.modak.backend.dto.NoticeDto;
import com.modak.backend.entity.NoticeEntity;
import com.modak.backend.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

//    //공지사항 등록
//    public void saveNotice(NoticeDto noticeDto) {
//        NoticeEntity noticeEntity = new Entity(
//    }

    //공지사항 상세조회
    public NoticeDto getNotice(Long noticeNo) {
        Optional<NoticeEntity> noticeEntity = noticeRepository.findById(noticeNo);
        NoticeEntity notice = noticeEntity.get();

        //Entitiy를 Dto로 변환
        NoticeDto noticeDto = NoticeDto.builder()
                .noticeNo(notice.getNoticeNo())
                .title(notice.getTitle())
                .content(notice.getContent())
                .imgName(notice.getImgName())
                .build();

        return noticeDto;
    }

    //공지사항 전체조회
    public List<NoticeDto> getAllNotice() {
        List<NoticeEntity> noticeEntityList = noticeRepository.findAll();

        List<NoticeDto> noticeDtoList = noticeEntityList.stream()
                .map(notice -> NoticeDto.builder()
                        .noticeNo(notice.getNoticeNo())
                        .title(notice.getTitle())
                        .content(notice.getContent())
                        .imgName(notice.getImgName())
                        .build())
                .collect(Collectors.toList());

        return noticeDtoList;
    }

    //공지사항 수정
//    public void modifyNotice(NoticeEntity notice) {
//        noticeRepository.save(notice);
//    }

    //공지사항 삭제
//    public void deleteNotice(int noticeNo) {
//        noticeRepository.deleteById(noticeNo);
//    }

}

