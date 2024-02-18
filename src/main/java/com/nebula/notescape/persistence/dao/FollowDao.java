package com.nebula.notescape.persistence.dao;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Follow;
import com.nebula.notescape.persistence.key.FollowKey;
import com.nebula.notescape.persistence.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class FollowDao implements Dao<Follow, FollowKey> {

  private final FollowRepository followRepository;

  @Override
  public Follow save(Follow entity) {
    return followRepository.save(entity);
  }

  @Override
  public Boolean existsById(FollowKey followKey) {
    return followRepository.existsById(followKey);
  }

  @Override
  public Optional<Follow> getById(FollowKey followKey) {
    return followRepository.findById(followKey);
  }

  public Page<Follow> getFollowingsByUserId(Long userId, Pageable pageable) {
    return followRepository.findAllByFollowerId(userId, pageable);
  }

  public Page<Follow> getFollowersByUserId(Long userId, Pageable pageable) {
    return followRepository.findAllByFollowingId(userId, pageable);
  }

  @Override
  public Follow update(Follow entity) {
    return followRepository.save(entity);
  }

  @Override
  public void deleteById(FollowKey followKey) {
    followRepository
        .findById(followKey)
        .ifPresent(follow -> {
          follow.setUpdateDate(LocalDateTime.now());
          follow.setRecordState(RecordState.DELETED);
          followRepository.save(follow);
        });
  }

}
