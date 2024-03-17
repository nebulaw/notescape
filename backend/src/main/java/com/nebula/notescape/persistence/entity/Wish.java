package com.nebula.notescape.persistence.entity;

import com.nebula.notescape.persistence.BaseEntity;
import com.nebula.notescape.persistence.key.WishKey;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wishes")
public class Wish extends BaseEntity {

  @EmbeddedId
  private WishKey id;

  @ManyToOne
  @MapsId("userId")
  private User user;

  @ManyToOne
  @JoinColumn(name = "movie_tmdb_id", referencedColumnName= "tmdb_id")
  private Movie movie;

}
