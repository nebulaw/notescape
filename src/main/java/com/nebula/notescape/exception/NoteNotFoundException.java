package com.nebula.notescape.exception;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException() {
        super("Note not found");
    }

}

