package com.foxminded.dao;

import com.foxminded.domain.Group;
import com.foxminded.domain.Student;

import java.util.List;

public interface GroupDao extends Dao<Group, Integer> {
    Group getByName(String name);

    List<Student> getStudents();

}
