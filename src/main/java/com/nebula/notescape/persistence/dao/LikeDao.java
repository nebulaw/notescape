package com.nebula.notescape.persistence.dao;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Like;
import com.nebula.notescape.persistence.key.LikeKey;
import com.nebula.notescape.persistence.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class LikeDao implements Dao<Like, LikeKey> {

  private final LikeRepository likeRepository;

  @Override
  public Like save(Like entity) {
    return likeRepository.save(entity);
  }

  @Override
  public Boolean existsById(LikeKey likeKey) {
    return likeRepository.existsById(likeKey);
  }

  @Override
  public Optional<Like> getById(LikeKey likeKey) {
    return likeRepository.findById(likeKey);
  }

  @Override
  public Like update(Like entity) {
    return likeRepository.save(entity);
  }

  @Override
  public void deleteById(LikeKey likeKey) {
    likeRepository
        .findById(likeKey)
        .ifPresent(like -> {
          like.setUpdateDate(LocalDateTime.now());
          like.setRecordState(RecordState.DELETED);
          likeRepository.save(like);
        });
  }
  
}
