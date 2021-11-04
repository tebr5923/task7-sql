package com.foxminded.integration;

import com.foxminded.assigner.Assigner;
import com.foxminded.assigner.CourseAssigner;
import com.foxminded.assigner.GroupAssigner;
import com.foxminded.dao.*;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.generator.CourseGenerator;
import com.foxminded.generator.GroupGenerator;
import com.foxminded.generator.StudentGenerator;
import com.foxminded.reader.ResourceFileReader;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleIntegrationTest {
    private static final Random RANDOM = new Random(10);
    private static final int COUNT_OF_GROUPS = 10;
    private static final int COUNT_OF_COURSES = 10;
    private static final int COUNT_OF_STUDENTS = 200;

    private static Connection connection;
    private static DBFactory dbFactory;
    private static StudentDao studentDao;
    private static GroupDao groupDao;
    private static CourseDao courseDao;

    @BeforeAll
    private static void createTables() throws DaoException {
        ConnectionProvider connectionProvider = new PropertyConnectionProvider();
        dbFactory = new DBFactory(connectionProvider);
        System.out.println("try create table...");
        dbFactory.createTables();
        connection = connectionProvider.getConnection();
        System.out.println("Connection to H2 open");

        studentDao = new StudentDaoImpl(connectionProvider);
        groupDao = new GroupDaoImpl(connectionProvider, studentDao);
        courseDao = new CourseDaoImpl(connectionProvider);



        //GroupGenerator and save
        List<Group> groupList = new GroupGenerator(RANDOM).generate(COUNT_OF_GROUPS);
        groupDao.saveAll(groupList);

        //CourseGenerator and save
        List<Course> courseList = new CourseGenerator(new ResourceFileReader()).generate(COUNT_OF_COURSES);
        courseDao.saveAll(courseList);

        //StudentGenerator and save
        List<Student> studentList = new StudentGenerator(new ResourceFileReader()).generate(COUNT_OF_STUDENTS);

        GroupAssigner groupAssigner = new GroupAssigner(RANDOM);
        List<Group> assignedGroupList = groupAssigner.assign(groupList, studentList);

        Assigner<Student, Course> courseAssigner = new CourseAssigner(RANDOM);
        List<Student> assignedStudentList = courseAssigner.assign(studentList, courseList);
        studentDao.saveAll(assignedStudentList);
    }

    @AfterAll
    private static void dropTables() throws SQLException {
        dbFactory.dropTables();
        connection.close();
        System.out.println("Connection to H2 close");
    }

/*
    @BeforeEach
    void setUp() throws DaoException {
        //GroupGenerator and save
        List<Group> groupList = new GroupGenerator(RANDOM).generate(COUNT_OF_GROUPS);
        groupDao.saveAll(groupList);

        //CourseGenerator and save
        List<Course> courseList = new CourseGenerator(new ResourceFileReader()).generate(COUNT_OF_COURSES);
        courseDao.saveAll(courseList);

        //StudentGenerator and save
        List<Student> studentList = new StudentGenerator(new ResourceFileReader()).generate(COUNT_OF_STUDENTS);

        GroupAssigner groupAssigner = new GroupAssigner(RANDOM);
        List<Group> assignedGroupList = groupAssigner.assign(groupList, studentList);

        Assigner<Student, Course> courseAssigner = new CourseAssigner(RANDOM);
        List<Student> assignedStudentList = courseAssigner.assign(studentList, courseList);
        studentDao.saveAll(assignedStudentList);
    }

    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM students_courses;\n" +
                "DELETE FROM students;\n" +
                "ALTER SEQUENCE students_id_seq RESTART WITH 1;\n" +
                "DELETE FROM groups\n;" +
                "ALTER SEQUENCE groups_id_seq RESTART WITH 101;\n" +
                "DELETE FROM courses;\n" +
                "ALTER SEQUENCE courses_id_seq RESTART WITH 1;\n";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("execute query for clear test data");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("WRONG QUERY");
        }
    }
*/

    @Test
    void main(){

        assertTrue(true);
    }
}
