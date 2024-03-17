package com.nebula.notescape.persistence.dao;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Wish;
import com.nebula.notescape.persistence.key.WishKey;
import com.nebula.notescape.persistence.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class WishDao implements Dao<Wish, WishKey> {

  private final WishRepository wishRepository;

  @Override
  public Wish save(Wish entity) {
    return wishRepository.save(entity);
  }

  @Override
  public Boolean existsById(WishKey wishKey) {
    return wishRepository.existsByIdAndRecordState(wishKey, RecordState.ACTIVE);
  }

  @Override
  public Optional<Wish> getById(WishKey wishKey) {
    return wishRepository.findById(wishKey);
  }

  @Override
  public Wish update(Wish entity) {
    return wishRepository.save(entity);
  }

  @Override
  public void deleteById(WishKey wishKey) {
    wishRepository.findById(wishKey)
        .ifPresent((wish) -> {
          wish.setRecordState(RecordState.DELETED);
          wish.setUpdateDate(LocalDateTime.now());
          wishRepository.save(wish);
        });
  }

  public Page<Wish> getWishPageByUserId(Long userId, Pageable pageable) {
    return wishRepository.findAllByUserId(userId, pageable);
  }
}
