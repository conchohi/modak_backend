package com.modak.backend.controller;

import com.modak.backend.dto.NoticeDto;
import com.modak.backend.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/{noticeNo}")
    public ResponseEntity<?> notice(@PathVariable Long noticeNo){
        NoticeDto noticeDto = noticeService.getNotice(noticeNo);

        return ResponseEntity.ok(noticeDto);
    }

    @GetMapping("/noticeAll")
    public ResponseEntity<?> noticeAll(){
        List<NoticeDto> noticeList = noticeService.getAllNotice();

        return ResponseEntity.ok(noticeList);
    }
}
