package com.nebula.notescape.persistence.repository;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Wish;
import com.nebula.notescape.persistence.key.WishKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface WishRepository extends
    JpaRepository<Wish, WishKey>,
    JpaSpecificationExecutor<Wish>,
    PagingAndSortingRepository<Wish, WishKey> {

  @Query("SELECT w " +
      "FROM Wish w " +
      "WHERE w.id.userId = :userId AND " +
      "w.recordState = 0")
  Page<Wish> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

  Boolean existsByIdAndRecordState(WishKey wishKey, RecordState recordState);

}