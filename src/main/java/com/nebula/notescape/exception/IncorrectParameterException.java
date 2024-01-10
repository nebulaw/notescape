package com.nebula.notescape.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class IncorrectParameterException extends RuntimeException {
    private Map<String, Object> parameters;

    public IncorrectParameterException() {
        super("Incorrect parameters");
        parameters = new HashMap<>();
    }

    public IncorrectParameterException parameter(String parameterName, Object value) {
        parameters.put(parameterName, value);
        return this;
    }

}
