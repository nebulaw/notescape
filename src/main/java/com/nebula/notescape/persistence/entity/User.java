package com.nebula.notescape.persistence.entity;

import com.nebula.notescape.payload.request.RegisterRequest;
import com.nebula.notescape.persistence.Authority;
import com.nebula.notescape.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @SequenceGenerator(name = "userSeqGen", sequenceName = "USERS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeqGen")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(length = 36)
    private String fullName;

    @Column(insertable = false, length = 240)
    private String about;

    private String imgUrl;

    @Column(nullable = false, length = 600)
    private String password;

    @Builder.Default
    private Integer noteCount = 0;

    @Builder.Default
    private Integer followingCount = 0;

    @Builder.Default
    private Integer followerCount = 0;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public static User of(RegisterRequest registerRequest) {
        return User.builder()
            .email(registerRequest.getEmail())
            .username(registerRequest.getUsername())
            .fullName(registerRequest.getFullName())
            .password(registerRequest.getPassword())
            .authority(Authority.USER)
            .imgUrl("")
            .noteCount(0)
            .followerCount(0)
            .followingCount(0)
            .build();
    }

}
