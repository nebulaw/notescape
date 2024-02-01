package com.nebula.notescape.persistence.dao;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.Note;
import com.nebula.notescape.persistence.entity.User;
import com.nebula.notescape.persistence.repository.NoteRepository;
import com.nebula.notescape.persistence.specification.NoteSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class NoteDao implements Dao<Note, Long> {

    private final NoteRepository noteRepository;
    private final NoteSpecification noteSpecification;

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

    public Page<Note> getPublicNotesByUser(User user, Pageable pageable) {
        Specification<Note> specification = Specification
                .where(noteSpecification.isActive())
                .and(noteSpecification.authorEquals(user))
                .and(noteSpecification.isPublic());
        return noteRepository.findAll(specification, pageable);
    }

    public Page<Note> getPublicNotesByMovieId(Long movieId, Pageable pageable) {
        Specification<Note> specification = Specification
                .where(noteSpecification.isActive())
                .and(noteSpecification.movieIdEquals(movieId))
                .and(noteSpecification.isPublic());
        return noteRepository.findAll(specification, pageable);
    }

    public Page<Note> getPrivateNotesByUser(User user, Pageable pageable) {
        Specification<Note> specification = Specification
                .where(noteSpecification.isActive())
                .and(noteSpecification.authorEquals(user))
                .and(noteSpecification.isPrivate());
        return noteRepository.findAll(specification, pageable);
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
