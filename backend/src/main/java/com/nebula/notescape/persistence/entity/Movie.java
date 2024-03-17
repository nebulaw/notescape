package com.nebula.notescape.persistence.entity;

import com.nebula.notescape.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "movies")
public class Movie extends BaseEntity {

  @Id
  @SequenceGenerator(name = "movieSeqGen", sequenceName = "movies_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movieSeqGen")
  private Long id;

  @Column(nullable = false, unique = true)
  private Long tmdbId;

  @Column(nullable = false)
  private String title;

  @Column(length = 1000)
  private String overview;

  private String releaseDate;

  private String posterPath;

  @Column(nullable = false)
  private String backdropPath;

  private Double voteAverage;

  private Long voteCount;

  private String originalLanguage;

  private String originalTitle;

}
