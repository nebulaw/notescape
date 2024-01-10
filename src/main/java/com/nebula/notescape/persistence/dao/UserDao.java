package com.nebula.notescape.persistence.dao;

import com.nebula.notescape.persistence.RecordState;
import com.nebula.notescape.persistence.entity.User;
import com.nebula.notescape.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserDao implements Dao<User, Long> {

    private final UserRepository userRepository;

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public Boolean existsById(Long id) {
        return userRepository.existsByIdAndRecordState(id, RecordState.ACTIVE);
    }

    public Boolean existsByUsername(String username) {
        return userRepository
                .existsByUsernameAndRecordState(username, RecordState.ACTIVE);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findByIdAndRecordState(id, RecordState.ACTIVE);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository
                .findByUsernameAndRecordState(username, RecordState.ACTIVE);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmailAndRecordState(email, RecordState.ACTIVE);
    }

    @Override
    public User update(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        Optional<User> userOptional = userRepository
                .findByIdAndRecordState(id, RecordState.ACTIVE);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRecordState(RecordState.DELETED);
            userRepository.save(user);
        }
    }

    public void deleteByUsername(String username) {
        Optional<User> userOptional = getByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRecordState(RecordState.DELETED);
            userRepository.save(user);
        }
    }
}
