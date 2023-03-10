package com.foxminded.dao;

import com.foxminded.domain.Course;
import com.foxminded.domain.Student;
import com.foxminded.mapper.Mapper;
import com.foxminded.mapper.StudentMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S106") //don't use logger in this task
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
        String sql = "UPDATE students set group_id=?, first_name=?, last_name=? WHERE id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            STUDENT_MAPPER.map(statement, model);
            if (statement.executeUpdate() == 0) {
                System.err.println("FAIL UPDATE!!! student with id " + model.getId() + " not exist");
                throw new DaoException("student not exist - update FAIL");
            }
            System.out.println("UPDATE OK... student with id " + model.getId());
        } catch (SQLException e) {
            System.err.println("cant update student");
            throw new DaoException("cant update student", e);
        }
        removeStudentFromAllCourses(model.getId());
        registerStudentToCourses(model.getId(), model.getCourses());
    }

    @Override
    public void delete(Student model) throws DaoException {
        String sql = "DELETE FROM students WHERE id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, model.getId());
            if (statement.executeUpdate() == 0) {
                System.err.println("FAIL DELETE!!! student with id " + model.getId() + " not exist");
                throw new DaoException("student not exist - delete FAIL");
            }
            System.out.println("DELETE OK... student with id " + model.getId());
        } catch (SQLException e) {
            System.err.println("cant delete student");
            throw new DaoException("cant delete student", e);
        }
    }

    @Override
    public void saveAll(List<Student> modelList) throws DaoException {
        String sql = "INSERT INTO students (id, group_id, first_name, last_name) values(DEFAULT,?,?,?);";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Student model : modelList) {
                checkCourses(model.getCourses());
                STUDENT_MAPPER.map(statement, model);
                statement.addBatch();
            }
            statement.executeBatch();
            System.out.println("All batches ok - SAVE all students");
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                for (Student model : modelList) {
                    if (generatedKeys.next()) {
                        model.setId(generatedKeys.getInt("id"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("all students NOT SAVE");
            throw new DaoException("all students NOT SAVE", e);
        }
        registerStudentsToCourses(modelList);
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

    @Override
    public List<Student> findStudentsByCourseName(String courseName) throws DaoException {
        List<Student> studentList = new ArrayList<>();
        String sql = "select s.id, s.group_id, s.first_name, s.last_name, sc.course_id, c.name as course_name, c.description from students s\n" +
                "left join students_courses as sc on sc.student_id=s.id\n" +
                "left join courses as c on c.id=sc.course_id\n" +
                "left join students_courses as sc1 on sc1.student_id=s.id\n" +
                "left join courses as c1 on c1.id=sc1.course_id where c1.name like ?;";

        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, courseName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = STUDENT_MAPPER.map(resultSet);
                    studentList.add(student);
                }
            }
        } catch (SQLException e) {
            System.err.println("cant find students by course name");
            throw new DaoException("cant find students by course name", e);
        }
        System.out.println("find students by course name OK...");
        return studentList;
    }

    private void checkCourses(List<Course> courses) throws DaoException {
        String sql = "select c.id from courses c;";
        List<Integer> listCourseIdFromDB = new ArrayList<>();
        List<Integer> listCourseIdToSave = courses.stream().map(Course::getId).collect(Collectors.toList());
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    listCourseIdFromDB.add(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("cant get courses");
            throw new DaoException("cant get courses", e);
        }
        if (!listCourseIdFromDB.containsAll(listCourseIdToSave)) {
            System.err.println("some courses not exist!!!");
            throw new DaoException("some courses not exist!!!");
        }
    }

    private void registerStudentToCourses(int studentId, List<Course> courses) {
        String sql = "INSERT INTO students_courses (student_id, course_id) values(?,?);";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Course course : courses) {
                statement.setInt(1, studentId);
                statement.setInt(2, course.getId());
                statement.addBatch();
            }
            statement.executeBatch();
            System.out.println("All batches ok - students_courses SAVE");
        } catch (SQLException e) {
            System.err.println("students_courses NOT SAVE");
            throw new IllegalStateException("students_courses NOT SAVE", e);
        }
    }

    private void registerStudentsToCourses(List<Student> students) {
        String sql = "INSERT INTO students_courses (student_id, course_id) values(?,?);";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Student student : students) {
                List<Course> courses = student.getCourses();
                for (Course course : courses) {
                    statement.setInt(1, student.getId());
                    statement.setInt(2, course.getId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
            System.out.println("All batches ok - students_courses SAVE");
        } catch (SQLException e) {
            System.err.println("students_courses NOT SAVE");
            throw new IllegalStateException("students_courses NOT SAVE", e);
        }
    }

    private void removeStudentFromAllCourses(int studentId) throws DaoException {
        String sql = "delete from students_courses where student_id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            if (statement.executeUpdate() == 0) {
                System.err.println("FAIL remove Student From All Courses");
                throw new DaoException("FAIL remove Student From All Courses");
            }
            System.out.println("Remove OK...");
        } catch (SQLException e) {
            System.err.println("FAIL remove Student From All Courses");
            throw new DaoException("FAIL remove Student From All Courses", e);
        }
    }
}
