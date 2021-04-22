package com.foxminded.dao;

import com.foxminded.domain.Course;
import com.foxminded.domain.Student;
import com.foxminded.mapper.CourseMapper;
import com.foxminded.mapper.Mapper;
import com.foxminded.mapper.StudentMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("squid:S106") //dont use logger in this task
public class StudentDaoImpl implements StudentDao {
    private static final Mapper<Student> STUDENT_MAPPER = new StudentMapper();
    private static final Mapper<Course> COURSE_MAPPER = new CourseMapper();
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
             final PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Student student = STUDENT_MAPPER.map(resultSet);
                    System.out.println("GET BY id OK... student with id " + id);
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
        List<Student> studentList = new ArrayList<>();
        String sql = "select s.id, s.group_id, s.first_name, s.last_name, sc.course_id, c.name as course_name, c.description from students s\n" +
                "left join students_courses as sc on sc.student_id=s.id  \n" +
                "left join courses as c on c.id=sc.course_id;\n";
        try (final Connection connection = connectionProvider.getConnection();
             final ResultSet resultSet = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql)) {
            while (resultSet.next()) {
                Student student = STUDENT_MAPPER.map(resultSet);
                studentList.add(student);
            }
        } catch (SQLException e) {
            System.err.println("cant get all students");
            throw new DaoException("cant get all students", e);
        }
        System.out.println("GET ALL students OK...");
        return studentList;
    }

    @Override
    public void save(Student model) throws DaoException {
        checkCourses(model.getCourses());
        String sql = "INSERT INTO students (id, group_id, first_name, last_name) values(DEFAULT,?,?,?);";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            STUDENT_MAPPER.map(statement, model);
            statement.executeUpdate();
            System.out.println("SAVE OK..." + " student with lastName " + model.getLastName());
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getInt("id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("cant save student");
            throw new DaoException("cant save student", e);
        }
        registerStudentToCourses(model.getId(), model.getCourses());
    }

    @Override
    public void update(Student model) throws DaoException {

    }

    @Override
    public void delete(Student model) throws DaoException {
        String sql = "DELETE FROM students WHERE id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, model.getId());
            if (statement.executeUpdate() == 0) {
                System.err.println("FAIL DELETE student " + model);
                throw new DaoException("student not exist - delete FAIL");
            }
            System.out.println("DELETE OK... student with id " + model.getId());
        } catch (SQLException e) {
            System.err.println("cant delete student");
            throw new DaoException("cant delete student", e);
        }

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

    private void checkCourses(List<Course> courses) throws DaoException {
        if (courses.isEmpty()) {
            return;
        }
        for (Course course : courses) {
            checkCourse(course);
        }
    }

    private void checkCourse(Course course) throws DaoException {
        String sql = "select c.id, c.name, c.description from courses c where c.id=?";
        Course courseFromDB = new Course();
        int id = course.getId();
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    courseFromDB = COURSE_MAPPER.map(resultSet);
                    System.out.println("GET BY id OK... course with id " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("cant get course by id!!!");
            throw new DaoException("cant get course by id!!!", e);
        }
        if (!course.equals(courseFromDB)) {
            System.err.println("dont have equal course in DB");
            throw new DaoException("dont have equal course in DB");
        }
    }

    private void registerStudentToCourses(int studentId, List<Course> courses) {
        if (courses.isEmpty()) {
            return;
        }
        for (Course course : courses) {
            registerStudentToCourse(studentId, course.getId());
        }
    }

    private void registerStudentToCourse(int studentId, int courseId) {
        String sql = "INSERT INTO students_courses (student_id, course_id) values(?,?);";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
            System.out.println("students_courses SAVE");
        } catch (SQLException e) {
            System.err.println("students_courses NOT SAVE");
            throw new IllegalStateException("students_courses NOT SAVE", e);
        }
    }
}
