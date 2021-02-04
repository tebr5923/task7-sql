package com.foxminded.dao;

import com.foxminded.domain.Model;

import java.util.List;

public interface Dao<T extends Model, K> {
    T getById(K id);

    List<T> getAll();

    void save(T model);

    void update(T model);

    void delete(T model);
}
