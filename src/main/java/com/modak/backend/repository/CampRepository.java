package com.modak.backend.repository;

import com.modak.backend.dtointerface.CampInterface;
import com.modak.backend.entity.CampEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampRepository extends JpaRepository<CampEntity,Long> {
    @Query(value = "select camp_no as campNo, camp_name as name, camp_address as address, img_name as imgName " +
            "from camp_tbl " +
            "where region in :regions and img_name != '' " +
            "order by Rand() limit 4", nativeQuery = true)
    public List<CampInterface> selectBestFourByBestRegions(@Param("regions") List<String> regions);

    @Query(value = "select camp_no as campNo, camp_name as name, camp_address as address, img_name as imgName " +
            "from camp_tbl c " +
            "where region = :region and img_name != '' " +
            "order by Rand() limit 4", nativeQuery = true)
    public List<CampInterface> selectBestFourByBestRegion(@Param("region") String region);


    @Query("select c from CampEntity c join c.campTypes t where t.type like CONCAT('%', :type, '%') and c.region like CONCAT('%', :region, '%') and c.name like CONCAT('%', :searchTerm, '%') ")
    public Page<CampEntity> getListByRegion(Pageable pageable, @Param("type") String type, @Param("region") String region, @Param("searchTerm") String searchTerm);

    @Query("select c from CampEntity c join c.campTypes t where t.type like CONCAT('%', :type, '%') and c.region in :regions and c.name like CONCAT('%', :searchTerm, '%') ")
    public Page<CampEntity> getListByWeather(Pageable pageable, @Param("type") String type, @Param("regions") List<String> regions, @Param("searchTerm") String searchTerm);


    public CampEntity getByCampNo(Long campNo);
}