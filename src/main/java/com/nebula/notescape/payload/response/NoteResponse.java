package com.nebula.notescape.payload.response;

import com.nebula.notescape.persistence.Access;
import com.nebula.notescape.persistence.entity.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteResponse {
    private Long id;
    private Long movieId;
    private UserResponse author;
    private String context;
    private Access access;
    private Long likeCount;
//    private Long parentId;

    public static NoteResponse of(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .movieId(note.getMovieId())
                .author(UserResponse.of(note.getAuthor()))
                .context(note.getContext())
                .access(note.getAccess())
                .likeCount(note.getLikeCount())
//                .parentId(note.getParentNote().getId())
                .build();
    }
}
