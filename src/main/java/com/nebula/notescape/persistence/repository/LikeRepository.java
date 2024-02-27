package com.nebula.notescape.persistence.repository;

import com.nebula.notescape.persistence.entity.Like;
import com.nebula.notescape.persistence.key.LikeKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, LikeKey> {

  @Query("SELECT l " +
      "FROM Like l " +
      "WHERE l.id.userId = :userId AND " +
      "l.recordState = 0")
  Page<Like> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

}
