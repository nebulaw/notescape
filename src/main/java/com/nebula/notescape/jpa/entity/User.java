package com.nebula.notescape.jpa.entity;

import com.nebula.notescape.jpa.Authority;
import com.nebula.notescape.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "USERS")
public class User extends BaseEntity {
    @Id
    @SequenceGenerator(name = "userSeqGen", sequenceName = "USERS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeqGen")
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 30)
    private String username;

    @Column(name = "FULL_NAME", length = 36)
    private String fullName;

    @Column(name = "ABOUT", insertable = false, length = 240)
    private String about;

    @Column(name = "IMG_URL")
    private String imgUrl;

    @Column(name = "PASSWORD", nullable = false, length = 600)
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

}
