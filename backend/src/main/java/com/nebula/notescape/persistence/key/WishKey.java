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
public class WishKey implements Serializable {
  private Long userId;
  private Long movieTmdbId;
}
