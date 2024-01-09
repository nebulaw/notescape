package com.nebula.notescape.jpa.repository;

import com.nebula.notescape.jpa.RecordState;
import com.nebula.notescape.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndRecordState(Long id, RecordState recordState);

    Optional<User> findByUsernameAndRecordState(String username, RecordState recordState);

    Optional<User> findByEmailAndRecordState(String email, RecordState recordState);

}
