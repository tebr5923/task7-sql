package com.foxminded.dao;

import java.util.List;

public interface Dao<T, K> {
    T getById(K id);

    List<T> getAll();

    void save(T model);

    void update(T model);

    void delete(T model);
}
