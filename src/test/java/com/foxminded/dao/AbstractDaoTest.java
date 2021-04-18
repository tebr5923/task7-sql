package com.foxminded.dao;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

abstract class AbstractDaoTest {
    protected static Connection connection;
    protected static DBFactory dbFactory;
    protected static Group group101;
    protected static Group group102;
    protected static Course history;
    protected static Course math;
    protected static Course economics;
    protected static Student ivanov;
    protected static Student petrov;
    protected static Student romanov;

    protected StudentDao studentDao;
    protected GroupDaoImpl groupDao;

    @BeforeAll
    public static void createTables() {
        dbFactory = new DBFactory(new ConnectionFactory());
        System.out.println("try create table...");
        dbFactory.createTables();
        connection = new ConnectionFactory().getConnection();
        System.out.println("Connection to H2 open");

        group101 = new Group();
        group101.setId(101);
        group101.setName("gr101");
        group102 = new Group();
        group102.setId(102);
        group102.setName("gr102");

        history = new Course();
        history.setId(1);
        history.setName("History");
        history.setDescription("this is history");
        math = new Course();
        math.setId(2);
        math.setName("Math");
        math.setDescription("this is math");
        economics = new Course();
        economics.setId(3);
        economics.setName("Economics");
        economics.setDescription("this is economics");

        ivanov = new Student();
        ivanov.setId(1);
        ivanov.setGroupId(101);
        ivanov.setFirstName("Ivan");
        ivanov.setLastName("Ivanov");
        ivanov.setCourses(Arrays.asList(history, math));
        petrov = new Student();
        petrov.setId(2);
        petrov.setGroupId(101);
        petrov.setFirstName("Petr");
        petrov.setLastName("Petrov");
        petrov.setCourses(Arrays.asList(math, economics));
        romanov = new Student();
        romanov.setId(3);
        romanov.setGroupId(101);
        romanov.setFirstName("Roman");
        romanov.setLastName("Romanov");
        romanov.setCourses(Arrays.asList(history, economics));
    }

    @AfterAll
    public static void dropTables() throws SQLException {
        dbFactory.dropTables();
        connection.close();
        System.out.println("Connection to H2 close");
    }

    @BeforeEach
    void setUp() {
        studentDao = new StudentDaoImpl(new ConnectionFactory());
        groupDao = new GroupDaoImpl(new ConnectionFactory(), new StudentDaoImpl(new ConnectionFactory()));

        String sql = "INSERT INTO groups (name) values('gr101');\n" +
                "INSERT INTO groups (name) values('gr102');\n" +
                "INSERT INTO students (first_name, last_name, group_id) values('Ivan','Ivanov',101);\n" +
                "INSERT INTO students (first_name, last_name, group_id) values('Petr','Petrov',101);\n" +
                "INSERT INTO students (first_name, last_name, group_id) values('Roman','Romanov',101);\n" +
                "INSERT INTO courses (name, description) values('History','this is history');\n" +
                "INSERT INTO courses (name, description) values('Math','this is math');\n" +
                "INSERT INTO courses (name, description) values('Economics','this is economics');\n" +
                "INSERT INTO students_courses  values(1,1);\n" +
                "INSERT INTO students_courses  values(1,2);\n" +
                "INSERT INTO students_courses  values(2,2);\n" +
                "INSERT INTO students_courses  values(2,3);\n" +
                "INSERT INTO students_courses  values(3,1);\n" +
                "INSERT INTO students_courses  values(3,3);\n";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("execute query for test data save");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("WRONG QUERY");
        }
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
}
