package com.foxminded.domain.loader;

import com.foxminded.dao.ConnectionProvider;
import com.foxminded.dao.DaoException;
import com.foxminded.domain.Student;
import com.foxminded.mapper.Mapper;
import com.foxminded.mapper.StudentMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("squid:S106") //dont use logger in this task
public class StudentsLoaderFromGroup implements Loader<Student> {
    private static final Mapper<Student> STUDENT_MAPPER = new StudentMapper();
    private final ConnectionProvider connectionProvider;

    public StudentsLoaderFromGroup(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
    @Override
    public List<Student> load(int id) throws DaoException {
        List<Student> studentList = new ArrayList<>();
        String sql = "select s.id, s.group_id, s.first_name, s.last_name from students s where s.group_id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = STUDENT_MAPPER.map(resultSet);
                    studentList.add(student);
                }
            }
        } catch (SQLException e) {
            System.err.println("cant load students");
            throw new DaoException("cant load students", e);
        }
        System.out.println("GET ALL students OK...");
        return studentList;
    }
}
