package com.nebula.notescape.persistence.entity;


import com.nebula.notescape.persistence.Access;
import com.nebula.notescape.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
// @Entity
public class Note extends BaseEntity {
    @Id
    @SequenceGenerator(name = "noteSeqGen", sequenceName = "NOTE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noteSeqGen")
    @Column(name = "ID")
    private Long id;

    @Column(name = "MOVIE_ID", updatable = false)
    private Long movieId;

    @ManyToOne
    @Column(name = "AUTHOR", updatable = false, nullable = false)
    private User author;

    @Lob
    @Column(name = "CONTEXT", nullable = false)
    private String context;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCESS", nullable = false)
    private Access access;


}
