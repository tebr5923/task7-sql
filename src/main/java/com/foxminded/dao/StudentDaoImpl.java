package com.foxminded.dao;

import com.foxminded.domain.Student;

import java.util.List;
import java.util.Optional;

public class StudentDaoImpl implements StudentDao {
    @Override
    public Optional<Student> getById(Integer id) throws DaoException {
        return Optional.empty();
    }

    @Override
    public List<Student> getAll() throws DaoException {
        return null;
    }

    @Override
    public void save(Student model) throws DaoException {

    }

    @Override
    public void update(Student model) throws DaoException {

    }

    @Override
    public void delete(Student model) throws DaoException {

    }

    @Override
    public List<Student> getStudentsByGroup(String groupName) throws DaoException {
        return null;
    }

    @Override
    public List<Student> getStudentsByGroup(int groupId) throws DaoException {
        return null;
    }
}
