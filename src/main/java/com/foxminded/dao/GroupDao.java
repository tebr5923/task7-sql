package com.foxminded.dao;

import com.foxminded.domain.Group;

public interface GroupDao extends Dao<Group, Integer> {
    Group getByName(String name);

}
