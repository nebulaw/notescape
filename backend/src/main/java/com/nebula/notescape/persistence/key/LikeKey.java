package com.nebula.notescape.persistence.key;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class LikeKey implements Serializable {
  private Long userId;
  private Long noteId;
}
