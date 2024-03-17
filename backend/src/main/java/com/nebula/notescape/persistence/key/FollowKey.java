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
public class FollowKey implements Serializable {
  private Long followerId;
  private Long followingId;
}
