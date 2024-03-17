package com.nebula.notescape.payload.response;

import com.nebula.notescape.persistence.Access;
import com.nebula.notescape.persistence.NoteType;
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
  private NoteType noteType;
  private Long movieId;
  private String movieName;
  private UserResponse author;
  private String context;
  private Access access;
  private Long likeCount;
  private Long parentId;
  private String createDate;
  private String updateDate;
  private Boolean isLiked;

  public static NoteResponse of(Note note) {
    return NoteResponse.builder()
        .id(note.getId())
        .noteType(note.getNoteType())
        .movieId(note.getMovieId())
        .movieName(note.getMovieName())
        .author(UserResponse.of(note.getAuthor()))
        .context(note.getContext())
        .access(note.getAccess())
        .likeCount(note.getLikeCount())
        .parentId(note.getParentNote() != null ?
            note.getParentNote().getId() : null)
        .createDate(note.getCreateDate().toString())
        .updateDate(note.getUpdateDate() != null ?
            note.getUpdateDate().toString() : "")
        .isLiked(Boolean.FALSE)
        .build();
  }
}
