package com.foxminded.dao;

import com.foxminded.domain.Student;

import java.util.List;

public interface StudentDao extends Dao<Student, Integer> {

    List<Student> getStudentsByGroup(int groupId) throws DaoException;

    List<Student> findStudentsByCourseName(String courseName) throws DaoException;
}
