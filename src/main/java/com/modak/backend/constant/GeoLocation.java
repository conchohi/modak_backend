package com.modak.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GeoLocation {
    private String region;
    private double lat;
    private double lon;

    public static List<GeoLocation> getGeoList(){
        List<GeoLocation> locations = new ArrayList<>();

        locations.add(new GeoLocation("서울시",37.5635694444444,126.980008333333));
        locations.add(new GeoLocation("부산시",35.1770194444444,129.076952777777));
        locations.add(new GeoLocation("대구시",35.8685416666666,128.603552777777));
        locations.add(new GeoLocation("인천시",37.4532333333333,126.707352777777));
        locations.add(new GeoLocation("광주시",35.1569749999999,126.853363888888));
        locations.add(new GeoLocation("대전시",36.3471194444444,127.386566666666));
        locations.add(new GeoLocation("울산시",35.5354083333333,129.313688888888));
        locations.add(new GeoLocation("세종시",36.4800121,127.2890691));
        locations.add(new GeoLocation("경기도",37.2718444444444,127.011688888888));
        locations.add(new GeoLocation("충청북도",36.6325,127.493586111111));
        locations.add(new GeoLocation("충청남도",36.3238722222222,127.422955555555));
        locations.add(new GeoLocation("전라북도",35.817275,127.111052777777));
        locations.add(new GeoLocation("전라남도",34.8130444444444,126.465));
        locations.add(new GeoLocation("경상북도",36.5759985118295,128.505832256098));
        locations.add(new GeoLocation("경상남도",35.2347361111111,128.694166666666));
        locations.add(new GeoLocation("제주도",33.4856944444444,126.500333333333));
        locations.add(new GeoLocation("강원도",37.8826916666666,127.731975));

        return locations;

    }
}
