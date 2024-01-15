package com.nebula.notescape.persistence.dao;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Note;
import com.nebula.notescape.persistence.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class NoteDao implements Dao<Note, Long> {

    private final NoteRepository noteRepository;

    @Override
    public Note save(Note entity) {
        return noteRepository.save(entity);
    }

    @Override
    public Boolean existsById(Long id) {
        return noteRepository.existsByIdAndRecordState(id, RecordState.ACTIVE);
    }

    @Override
    public Optional<Note> getById(Long id) {
        return noteRepository.findByIdAndRecordState(id, RecordState.ACTIVE);
    }

    @Override
    public Note update(Note entity) {
        return noteRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Note> noteOptional = getById(id);
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            note.setUpdateDate(LocalDateTime.now());
            note.setRecordState(RecordState.DELETED);
            noteRepository.save(note);
        }
    }

}
