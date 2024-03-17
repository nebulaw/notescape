package com.nebula.notescape.persistence.entity;

import com.nebula.notescape.persistence.BaseEntity;
import com.nebula.notescape.persistence.key.WatchKey;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "watches")
public class Watch extends BaseEntity {

  @EmbeddedId
  private WatchKey id;

  @ManyToOne
  @MapsId("userId")
  private User user;

  @ManyToOne
  @JoinColumn(name = "movie_tmdb_id", referencedColumnName= "tmdbId")
  private Movie movie;

}
