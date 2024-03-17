package com.nebula.notescape.persistence.dao;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Watch;
import com.nebula.notescape.persistence.key.WatchKey;
import com.nebula.notescape.persistence.repository.WatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class WatchDao implements Dao<Watch, WatchKey> {

  private final WatchRepository watchRepository;

  @Override
  public Watch save(Watch entity) {
    return watchRepository.save(entity);
  }

  @Override
  public Boolean existsById(WatchKey watchKey) {
    return watchRepository.existsById(watchKey);
  }

  @Override
  public Optional<Watch> getById(WatchKey watchKey) {
    return watchRepository.findById(watchKey);
  }

  public Page<Watch> getWatchPageByUserId(Long userId, Pageable pageable) {
    return watchRepository.findAllByUserId(userId, pageable);
  }

  @Override
  public Watch update(Watch entity) {
    return watchRepository.save(entity);
  }

  @Override
  public void deleteById(WatchKey watchKey) {
    watchRepository.findById(watchKey)
        .ifPresent((watch) -> {
          watch.setRecordState(RecordState.DELETED);
          watch.setUpdateDate(LocalDateTime.now());
          watchRepository.save(watch);
        });
  }
}
