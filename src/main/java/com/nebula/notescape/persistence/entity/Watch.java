package com.nebula.notescape.persistence.entity;

import com.nebula.notescape.persistence.BaseEntity;
import com.nebula.notescape.persistence.key.WatchKey;
import jakarta.persistence.*;
import lombok.*;
import org.testcontainers.shaded.org.checkerframework.checker.optional.qual.MaybePresent;

import java.io.Serializable;

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

}
