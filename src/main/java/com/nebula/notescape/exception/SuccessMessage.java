package com.nebula.notescape.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {
    NOTE_CREATED("Note has been successfully created"),
    NOTE_UPDATED("Note has been successfully updated"),
    USER_CREATED("User has been successfully created"),
    USER_UPDATED("User has been successfully updated");
    private final String message;
}
