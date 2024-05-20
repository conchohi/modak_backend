package com.modak.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CampApiServiceTest {
    @Autowired
    CampApiService campApiService;

//    @Test
//    void getCampTotalCount() {
//        log.info(campApiService.getCampTotalCount());
//    }

    @Test
    void registerByApi(){
        campApiService.registerByApi();
    }



}