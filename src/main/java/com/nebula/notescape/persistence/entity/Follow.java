package com.nebula.notescape.persistence.entity;

import com.nebula.notescape.persistence.BaseEntity;
import com.nebula.notescape.persistence.key.FollowKey;
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
public class Follow extends BaseEntity {

  @EmbeddedId
  private FollowKey id;

  @ManyToOne
  private User from;

  @ManyToOne
  private User to;

}
