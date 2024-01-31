package com.nebula.notescape.exception;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(Long id) {
        super("Note not found by id{%d}".formatted(id));
    }

}

