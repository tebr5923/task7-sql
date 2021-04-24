package com.foxminded.dao;

import com.foxminded.domain.Course;
import com.foxminded.mapper.CourseMapper;
import com.foxminded.mapper.Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("squid:S106") //dont use logger in this task
public class CourseDaoImpl implements CourseDao {
    private static final Mapper<Course> COURSE_MAPPER = new CourseMapper();
    private final ConnectionProvider connectionProvider;

    public CourseDaoImpl(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Course> getByName(String name) throws DaoException {
        String sql = "select c.id, c.name, c.description from courses c where c.name=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Course course = COURSE_MAPPER.map(resultSet);
                    System.out.println("GET BY name OK... course with name " + name);
                    return Optional.of(course);
                }
            }
        } catch (SQLException e) {
            System.err.println("cant get course by name!!!");
            throw new DaoException("cant get course by name!!!", e);
        }
        System.err.println("NOT FOUND!!!!... course with name " + name);
        return Optional.empty();
    }

    @Override
    public Optional<Course> getById(Integer id) throws DaoException {
        String sql = "select c.id, c.name, c.description from courses c where c.id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Course course = COURSE_MAPPER.map(resultSet);
                    System.out.println("GET BY ID OK... course with id " + id);
                    return Optional.of(course);
                }
            }
        } catch (SQLException e) {
            System.err.println("cant get course by id!!!");
            throw new DaoException("cant get course by id!!!", e);
        }
        System.err.println("NOT FOUND!!!!... course with id " + id);
        return Optional.empty();
    }

    @Override
    public List<Course> getAll() throws DaoException {
        List<Course> courseList = new ArrayList<>();
        String sql = "select c.id, c.name, c.description from courses c;";
        try (final Connection connection = connectionProvider.getConnection();
             final ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                Course course = COURSE_MAPPER.map(resultSet);
                courseList.add(course);
            }
        } catch (SQLException e) {
            System.err.println("cant get all courses");
            throw new DaoException("cant get all courses", e);
        }
        System.out.println("GET ALL courses OK...");
        return courseList;
    }

    @Override
    public void save(Course model) throws DaoException {
        String sql = "INSERT INTO courses (id, name, description) values(DEFAULT,?,?);";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            COURSE_MAPPER.map(statement, model);
            statement.executeUpdate();
            System.out.println("SAVE OK... course with name " + model.getName());
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("cant save course");
            throw new DaoException("cant save course", e);
        }
    }

    @Override
    public void update(Course model) throws DaoException {
        String sql = "UPDATE courses set name=?, description=?  WHERE id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            COURSE_MAPPER.map(statement, model);
            if (statement.executeUpdate() == 0) {
                System.err.println("FAIL UPDATE!!! course not exist " + model);
                throw new DaoException("course not exist - UPDATE FAIL");
            }
            System.out.println("UPDATE OK... course " + model);
        } catch (SQLException e) {
            System.err.println("cant update course");
            throw new DaoException("cant update course", e);
        }
    }

    @Override
    public void delete(Course model) throws DaoException {
        String sql = "DELETE FROM courses WHERE id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, model.getId());
            if (statement.executeUpdate() == 0) {
                System.err.println("FAIL DELETE course " + model);
                throw new DaoException("course not exist - delete FAIL");
            }
            System.out.println("DELETE OK... course " + model);
        } catch (SQLException e) {
            System.err.println("cant delete course");
            throw new DaoException("cant delete course", e);
        }
    }
}
