package com.modak.backend.controller;

import com.modak.backend.dto.FavoriteDto;
import com.modak.backend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/{campNo}")
    public ResponseEntity<?> addFavorite(@PathVariable("campNo") Long campNo) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setCampNo(campNo);
        favoriteDto.setUserId(id);
        favoriteService.addFavorite(favoriteDto);
        return ResponseEntity.ok(Map.of("message","success"));
    }

    @DeleteMapping("/{campNo}")
    public ResponseEntity<?> removeFavorite(@PathVariable("campNo") Long campNo) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setCampNo(campNo);
        favoriteDto.setUserId(id);
        try{
             favoriteService.removeFavorite(favoriteDto);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","error"));
        }
        return ResponseEntity.ok(Map.of("message","success"));
    }

    @GetMapping("/{campNo}")
    public ResponseEntity<?> isLike(@PathVariable("campNo") Long campNo){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setUserId(id);
        favoriteDto.setCampNo(campNo);
        boolean isLike = favoriteService.getFavorite(favoriteDto);
        return ResponseEntity.ok(Map.of("isLike",isLike));
    }

}
