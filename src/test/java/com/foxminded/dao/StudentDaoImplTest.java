package com.foxminded.dao;

import com.foxminded.domain.Student;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDaoImplTest extends AbstractDaoTest{
    private StudentDao studentDao;

    @BeforeEach
    void setUp() {
        studentDao = new StudentDaoImpl(new ConnectionFactory());
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
    void getStudentsByGroup_shouldReturnStudents_whenLoadingStudentsIsExist() throws DaoException {
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

        List<Student> actual = studentDao.getStudentsByGroup(101);

        assertEquals(expected, actual);
    }
}
