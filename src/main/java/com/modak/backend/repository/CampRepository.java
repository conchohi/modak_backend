package com.modak.backend.repository;

import com.modak.backend.dtointerface.CampInterface;
import com.modak.backend.entity.CampEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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


    //오버로딩 (type 이 필터에 없을 경우 페이징 처리를 위해 분리)
    @Query("select c from CampEntity c left join fetch c.reviewList join c.campTypes t where t.type = :type and c.region like CONCAT('%', :region, '%') and c.name like CONCAT('%', :searchTerm, '%') ")
    public Page<CampEntity> getListByRegion(Pageable pageable, @Param("type") String type, @Param("region") String region, @Param("searchTerm") String searchTerm);

    @Query("select c from CampEntity c left join fetch c.reviewList where c.region like CONCAT('%', :region, '%') and c.name like CONCAT('%', :searchTerm, '%') ")
    public Page<CampEntity> getListByRegion(Pageable pageable, @Param("region") String region, @Param("searchTerm") String searchTerm);

    //오버로딩 (type 이 필터에 없을 경우 페이징 처리를 위해 분리)
    @Query("select c from CampEntity c left join fetch c.reviewList join c.campTypes t where t.type = :type and c.region in :regions and c.name like CONCAT('%', :searchTerm, '%') ")
    public Page<CampEntity> getListByWeather(Pageable pageable, @Param("type") String type, @Param("regions") List<String> regions, @Param("searchTerm") String searchTerm);
    @Query("select c from CampEntity c left join fetch c.reviewList where c.region in :regions and c.name like CONCAT('%', :searchTerm, '%') ")
    public Page<CampEntity> getListByWeather(Pageable pageable, @Param("regions") List<String> regions, @Param("searchTerm") String searchTerm);

    @Query("select c from CampEntity c left join fetch c.reviewList r where c.campNo = :campNo order by r.reviewNo desc ")
    public CampEntity getByCampNo(@Param("campNo") Long campNo);
}