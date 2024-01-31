package com.nebula.notescape.payload.request;

import com.nebula.notescape.persistence.Access;
import com.nebula.notescape.persistence.NoteType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NoteRequest {
    private Long movieId;
    private NoteType noteType;
    private String movieName;
    private String email;
    private String context;
    private Access access;
    private Long likeCount;
    private Long parentId;
}
