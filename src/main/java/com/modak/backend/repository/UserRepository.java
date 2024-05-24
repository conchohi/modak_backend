package com.modak.backend.repository;

import com.modak.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUserId(String userId);

    UserEntity findByUserId(String userId);

    @Query("select u from user u left join fetch u.favorites f where u.userId = :userId order by f.favoriteNo desc")
    UserEntity findUserAndFavoriteByUserId(@Param("userId") String userId);

    UserEntity findByEmail(String email);

}
