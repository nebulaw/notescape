package com.nebula.notescape.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "CREATE_DATE", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    protected LocalDateTime createDate = LocalDateTime.now();


    @LastModifiedDate
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    protected LocalDateTime updateDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RECORD_STATE", nullable = false)
    @JsonIgnore
    protected RecordState recordState;
}
