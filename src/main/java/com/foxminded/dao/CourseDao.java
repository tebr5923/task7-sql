package com.foxminded.dao;

import com.foxminded.domain.Course;

import java.util.Optional;

public interface CourseDao extends Dao<Course, Integer> {
    Optional<Course> getByName(String name) throws DaoException;

}
