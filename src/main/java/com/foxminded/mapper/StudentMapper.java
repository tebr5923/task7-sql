package com.foxminded.mapper;

import com.foxminded.domain.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper implements Mapper<Student> {
    @Override
    public Student map(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setGroupId(resultSet.getInt("group_id"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));

        return student;
    }

    @Override
    public void map(PreparedStatement statement, Student model) throws SQLException {
        statement.setString(1, model.getFirstName());
        statement.setString(2, model.getLastName());
        statement.setInt(3, model.getGroupId());
        if (model.getId() != 0) {
            statement.setInt(4, model.getId());
        }
    }
}
