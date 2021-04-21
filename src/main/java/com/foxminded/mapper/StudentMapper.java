package com.foxminded.mapper;

import com.foxminded.domain.Course;
import com.foxminded.domain.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentMapper implements Mapper<Student> {


    @Override
    public Student map(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        int id = resultSet.getInt("id");
        student.setId(id);
        student.setGroupId(resultSet.getInt("group_id"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));
        List<Course> courseList = new ArrayList<>();
       do {
            Course course = new Course();
            course.setId(resultSet.getInt("course_id"));
            course.setName(resultSet.getString("course_name"));
            course.setDescription(resultSet.getString("description"));
            courseList.add(course);
        } while (resultSet.next() && resultSet.getInt("id") == id);
        if (!resultSet.isAfterLast()){
            resultSet.previous();
        }
        student.setCourses(courseList);
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
