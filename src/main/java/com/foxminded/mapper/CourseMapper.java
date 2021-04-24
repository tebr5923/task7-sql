package com.foxminded.mapper;

import com.foxminded.domain.Course;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper implements Mapper<Course> {
    @Override
    public Course map(ResultSet resultSet) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getInt("id"));
        course.setName(resultSet.getString("name"));
        course.setDescription(resultSet.getString("description"));
        return course;
    }

    @Override
    public void map(PreparedStatement statement, Course model) throws SQLException {
        statement.setString(1, model.getName());
        statement.setString(2, model.getDescription());
        if (model.getId() != 0) {
            statement.setInt(3, model.getId());
        }
    }
}
