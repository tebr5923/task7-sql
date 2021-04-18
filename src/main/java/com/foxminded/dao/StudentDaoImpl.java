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

@SuppressWarnings("squid:S106") //dont use logger in this task
public class StudentDaoImpl implements StudentDao {
    private static final Mapper<Student> STUDENT_MAPPER = new StudentMapper();
    private final ConnectionProvider connectionProvider;

    public StudentDaoImpl(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Student> getById(Integer id) throws DaoException {
        String sql = "select s.id, s.group_id, s.first_name, s.last_name, sc.course_id, c.name as course_name, c.description from students s\n" +
                "left join students_courses as sc on sc.student_id=s.id  \n" +
                "left join courses as c on c.id=sc.course_id where s.id=?;\n";

        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Student student = STUDENT_MAPPER.map(resultSet);
                    System.out.println("GET BY id OK... student with name " + id);
                    return Optional.of(student);
                }
            }
        } catch (SQLException e) {
            System.err.println("cant get student by id!!!");
            throw new DaoException("cant get student by id!!!", e);
        }
        System.err.println("NOT FOUND!!!!... student with id " + id);
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
        String sql = "select s.id, s.group_id, s.first_name, s.last_name, sc.course_id, c.name as course_name, c.description from students s\n" +
                "left join students_courses as sc on sc.student_id=s.id  \n" +
                "left join courses as c on c.id=sc.course_id where s.group_id=?;\n";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
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
