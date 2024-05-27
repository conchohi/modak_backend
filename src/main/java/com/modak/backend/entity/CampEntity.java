package com.modak.backend.entity;

import com.modak.backend.entity.embeddable.CampFacility;
import com.modak.backend.entity.embeddable.CampType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = {"facilities","campTypes"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="camp_tbl")
public class CampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campNo;

    @Column(name = "camp_name", nullable = false)
    private String name;

    @Column(name = "camp_address")
    private String address;

    @Column(name = "camp_phone")
    private String phone;

    private String lineIntro;

    @Column(length = 5000)
    private String intro;
    private double lat;
    private double lon;
    private String region;
    private String homePage;
    private String imgName;

    @OneToMany(mappedBy = "camp", cascade = CascadeType.ALL
            , orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ReviewEntity> reviewList;


    @ElementCollection
    @Builder.Default
    @CollectionTable(name = "camp_facility")
    private List<CampFacility> facilities = new ArrayList<>();


    @ElementCollection
    @Builder.Default
    @CollectionTable(name = "camp_type")
    private List<CampType> campTypes = new ArrayList<>();

    public void addFacility(String facility){
        CampFacility campFacility = new CampFacility(facility);
        this.facilities.add(campFacility);
    }
    public void addType(String type){
//        CampingType campingType = CampingType.valueOf(type);
        CampType campType = new CampType(type);
        this.campTypes.add(campType);
    }
}
