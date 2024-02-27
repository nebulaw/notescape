package com.nebula.notescape.persistence.entity;

import com.nebula.notescape.persistence.BaseEntity;
import com.nebula.notescape.persistence.key.LikeKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Like extends BaseEntity {

  @EmbeddedId
  private LikeKey id;

  @ManyToOne
  private User from;

  @ManyToOne
  private Note to;

}
