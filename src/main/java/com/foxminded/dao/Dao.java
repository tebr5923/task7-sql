package com.foxminded.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, K> {
    Optional<T> getById(K id) throws DaoException;

    List<T> getAll() throws DaoException;

    void save(T model) throws DaoException;

    void update(T model) throws DaoException;

    void delete(T model) throws DaoException;

    void saveAll(List<T> modelList) throws DaoException;
}
