package com.nebula.notescape.persistence.dao;

import java.util.Optional;

public interface Dao<T, ID> {

    T save(T entity);

    Boolean existsById(ID id);

    Optional<T> getById(ID id);

    T update(T entity);

    void deleteById(ID id);

}
