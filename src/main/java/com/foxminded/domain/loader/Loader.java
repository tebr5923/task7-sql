package com.foxminded.domain.loader;

import com.foxminded.dao.DaoException;

import java.util.List;

public interface Loader<T> {
    List<T> load(int id) throws DaoException;
}
