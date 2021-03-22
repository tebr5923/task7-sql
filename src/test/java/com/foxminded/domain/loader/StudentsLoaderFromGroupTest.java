package com.foxminded.domain.loader;

import com.foxminded.dao.ConnectionFactory;
import com.foxminded.dao.DBFactory;
import com.foxminded.dao.DaoException;
import com.foxminded.domain.Student;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentsLoaderFromGroupTest {
    private static Connection connection;
    private static DBFactory dbFactory;

    @BeforeAll
    public static void createTables() {
        //create Tables
        dbFactory = new DBFactory(new ConnectionFactory());
        System.out.println("try create table...");
        dbFactory.createTables();
        connection = new ConnectionFactory().getConnection();
        System.out.println("Connection to H2 open");
    }

    @AfterAll
    public static void dropTables() throws SQLException {
        //drop Tables
        dbFactory.dropTables();
        connection.close();
        System.out.println("Connection to H2 close");
    }

    @BeforeEach
    void setUp() {
        String sql = "INSERT INTO groups (name) values('grp11');\n" +
                "INSERT INTO groups (name) values('grp22');\n" +
                "INSERT INTO students (first_name, last_name, group_id) values('Ivan','Ivanov',101);\n" +
                "INSERT INTO students (first_name, last_name, group_id) values('Petr','Petrov',101);\n" +
                "INSERT INTO students (first_name, last_name, group_id) values('Roman','Romanov',101);\n";
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
        String sql = "DELETE FROM groups;ALTER SEQUENCE groups_id_seq RESTART WITH 101;\n" +
                "DELETE FROM students;";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("execute query for clear test data");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("WRONG QUERY");
        }
    }

    @Test
    void load_shouldReturnStudents_whenLoadingStudentsIsExist() throws DaoException {
        Student ivanov = new Student();
        ivanov.setId(1);
        ivanov.setGroupId(101);
        ivanov.setFirstName("Ivan");
        ivanov.setLastName("Ivanov");
        Student petrov = new Student();
        petrov.setId(2);
        petrov.setGroupId(101);
        petrov.setFirstName("Petr");
        petrov.setLastName("Petrov");
        Student romanov = new Student();
        romanov.setId(3);
        romanov.setGroupId(101);
        romanov.setFirstName("Roman");
        romanov.setLastName("Romanov");
        List<Student> expected = Arrays.asList(ivanov, petrov, romanov);

        StudentsLoaderFromGroup studentsLoader = new StudentsLoaderFromGroup(new ConnectionFactory());
        List<Student> actual = studentsLoader.load(101);

        assertEquals(expected, actual);
    }
}