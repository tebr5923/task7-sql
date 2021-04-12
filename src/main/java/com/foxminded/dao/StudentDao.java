package com.foxminded.dao;

import com.foxminded.domain.Student;

import java.util.List;

public interface StudentDao extends Dao<Student, Integer> {
    List<Student> getStudentsByGroup(String groupName) throws DaoException;

    List<Student> getStudentsByGroup(int groupId) throws DaoException;
}
