package com.nebula.notescape.persistence.repository;

import com.nebula.notescape.persistence.entity.Follow;
import com.nebula.notescape.persistence.entity.Like;
import com.nebula.notescape.persistence.key.FollowKey;
import com.nebula.notescape.persistence.key.LikeKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface FollowRepository extends
    JpaRepository<Follow, FollowKey>,
    JpaSpecificationExecutor<Follow>,
    PagingAndSortingRepository<Follow, FollowKey> {

  @Query("SELECT f " +
      "FROM Follow f " +
      "WHERE f.id.followerId = :followerId AND " +
      "f.recordState = 0")
  Page<Follow> findAllByFollowerId(@Param("followerId") Long followerId, Pageable pageable);

  @Query("SELECT f " +
      "FROM Follow f " +
      "WHERE f.id.followingId = :followingId AND " +
      "f.recordState = 0 AND f.following.recordState = 0")
  Page<Follow> findAllByFollowingId(@Param("followingId") Long followingId, Pageable pageable);

}
