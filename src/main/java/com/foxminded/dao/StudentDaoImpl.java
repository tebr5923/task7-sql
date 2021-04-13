package com.foxminded.dao;

import com.foxminded.domain.Student;
import com.foxminded.mapper.Mapper;
import com.foxminded.mapper.StudentMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDaoImpl implements StudentDao {
    private static final Mapper<Student> STUDENT_MAPPER = new StudentMapper();
    private final ConnectionProvider connectionProvider;

    public StudentDaoImpl(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

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
    public List<Student> getStudentsByGroup(int groupId) throws DaoException {
        List<Student> studentList = new ArrayList<>();
        String sql = "select s.id, s.group_id, s.first_name, s.last_name from students s where s.group_id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, groupId);
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
