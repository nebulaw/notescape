package com.nebula.notescape.persistence.repository;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByIdAndRecordState(Long id, RecordState recordState);

    Boolean existsByIdAndRecordState(Long id, RecordState recordState);

}
