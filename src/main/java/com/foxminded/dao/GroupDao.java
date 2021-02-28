package com.foxminded.dao;

import com.foxminded.domain.Group;

import java.util.Optional;

public interface GroupDao extends Dao<Group, Integer> {
    Optional<Group> getByName(String name);

}
