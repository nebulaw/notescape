package com.nebula.notescape.persistence.dao;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Movie;
import com.nebula.notescape.persistence.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MovieDao implements Dao<Movie, Long> {

  private final MovieRepository movieRepository;

  @Override public Movie save(Movie entity) {
    return movieRepository.save(entity);
  }

  @Override public Boolean existsById(Long id) {
    return movieRepository.existsById(id);
  }

  @Override public Optional<Movie> getById(Long id) {
    return movieRepository.findById(id);
  }

  public Optional<Movie> getByTmdbId(Long tmdbId) {
    return movieRepository.findByTmdbIdAndRecordState(tmdbId, RecordState.ACTIVE);
  }

  public Boolean existsByTmdbId(Long tmdbId) {
    return movieRepository.existsByTmdbIdAndRecordState(tmdbId, RecordState.ACTIVE);
  }

  @Override public Movie update(Movie entity) {
    return movieRepository.save(entity);
  }

  @Override public void deleteById(Long id) {
    movieRepository
        .findByIdAndRecordState(id, RecordState.ACTIVE)
        .ifPresent(movie -> {
          movie.setRecordState(RecordState.DELETED);
          movieRepository.save(movie);
        });
  }

  public void deleteByTmdbId(Long tmdbId) {
    movieRepository
        .findByTmdbIdAndRecordState(tmdbId, RecordState.ACTIVE)
        .ifPresent(movie -> {
          movie.setRecordState(RecordState.DELETED);
          movieRepository.save(movie);
        });
  }
}
