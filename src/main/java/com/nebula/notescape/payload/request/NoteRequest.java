package com.nebula.notescape.payload.request;

import com.nebula.notescape.persistence.Access;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NoteRequest {
    private Long movieId;
    private String email;
    private String context;
    private Access access;
//    private Long parentId;
}
