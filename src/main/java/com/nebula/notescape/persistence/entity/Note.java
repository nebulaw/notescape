package com.nebula.notescape.persistence.entity;


import com.nebula.notescape.persistence.Access;
import com.nebula.notescape.persistence.BaseEntity;
import com.nebula.notescape.persistence.NoteType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Length;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Note extends BaseEntity {

  @Id
  @SequenceGenerator(name = "noteSeqGen", sequenceName = "NOTE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noteSeqGen")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private NoteType noteType = NoteType.NOTE;

  @Column(updatable = false)
  private Long movieId;

  @Column(nullable = false, updatable = false)
  private String movieName;

  @ManyToOne
  private User author;

  @Lob
  @Column(nullable = false, length = Length.LONG)
  private String context;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Access access = Access.PRIVATE;

  @Builder.Default
  private Long likeCount = 0L;

  @ManyToOne(optional = true)
  @JoinColumn(updatable = false, nullable = true)
  @Builder.Default
  private Note parentNote = null;

}
