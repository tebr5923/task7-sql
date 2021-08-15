package com.foxminded.dao;

import com.foxminded.domain.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends Dao<Group, Integer> {
    Optional<Group> getByName(String name) throws DaoException;

    List<Group> findByStudentsCount(int studentCount) throws DaoException;
}
