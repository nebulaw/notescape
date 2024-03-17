package com.nebula.notescape.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomMessageException extends RuntimeException {
    private Object error;
    private HttpStatus status;
}
