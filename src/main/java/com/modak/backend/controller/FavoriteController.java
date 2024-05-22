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

    @PostMapping("")
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteDto favoriteDto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        favoriteDto.setUserId(id);
        return ResponseEntity.ok(Map.of("message","success"));
    }

    @DeleteMapping("")
    public ResponseEntity<?> removeFavorite(@RequestBody FavoriteDto favoriteDto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        favoriteDto.setUserId(id);
        try{
             favoriteService.removeFavorite(favoriteDto);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","error"));
        }
        return ResponseEntity.ok(Map.of("message","success"));
    }


}
