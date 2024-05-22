package com.modak.backend.repository;

import com.modak.backend.entity.CampEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CampRepositoryTest {

    @Autowired
    CampRepository campRepository;
    @Test
    void getByCampNo() {
        CampEntity camp = campRepository.getByCampNo(3999L);
        System.out.println(camp);
    }
}