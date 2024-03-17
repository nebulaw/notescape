package com.nebula.notescape.persistence.repository;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface MovieRepository extends
    JpaRepository<Movie, Long>,
    JpaSpecificationExecutor<Movie>,
    PagingAndSortingRepository<Movie, Long> {

  Optional<Movie> findByTmdbIdAndRecordState(Long tmdbId, RecordState recordState);

  Optional<Movie> findByIdAndRecordState(Long id, RecordState recordState);

  Boolean existsByIdAndRecordState(Long id, RecordState recordState);

  Boolean existsByTmdbIdAndRecordState(Long tmdbId, RecordState recordState);

}
