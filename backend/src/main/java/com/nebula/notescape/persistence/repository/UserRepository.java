package com.nebula.notescape.persistence.repository;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Long>,
        JpaSpecificationExecutor<User>,
        PagingAndSortingRepository<User, Long> {

    Optional<User> findByIdAndRecordState(Long id, RecordState recordState);

    Optional<User> findByUsernameAndRecordState(String username, RecordState recordState);

    Optional<User> findByEmailAndRecordState(String email, RecordState recordState);

    Boolean existsByUsernameAndRecordState(String username, RecordState recordState);

    Boolean existsByIdAndRecordState(Long id, RecordState recordState);

}
