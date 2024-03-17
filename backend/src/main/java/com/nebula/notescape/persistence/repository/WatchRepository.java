package com.nebula.notescape.persistence.repository;

import com.nebula.notescape.persistence.entity.Watch;
import com.nebula.notescape.persistence.key.WatchKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface WatchRepository extends
    JpaRepository<Watch, WatchKey>,
    JpaSpecificationExecutor<Watch>,
    PagingAndSortingRepository<Watch, WatchKey> {

  @Query("SELECT w " +
      "FROM Watch w " +
      "WHERE w.id.userId = :userId AND " +
      "w.recordState = 0")
  Page<Watch> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

}
