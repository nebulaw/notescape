package com.nebula.notescape.persistence.entity;

import com.nebula.notescape.persistence.BaseEntity;
import com.nebula.notescape.persistence.key.FollowKey;
import jakarta.persistence.*;
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
  @MapsId("followerId")
  @JoinColumn(name = "follower_id")
  private User follower;

  @ManyToOne
  @MapsId("followingId")
  @JoinColumn(name = "following_id")
  private User following;

}
