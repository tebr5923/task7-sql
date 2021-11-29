package com.foxminded.integration;

import com.foxminded.assigner.Assigner;
import com.foxminded.assigner.CourseAssigner;
import com.foxminded.assigner.GroupAssigner;
import com.foxminded.view.reader.ConsoleReader;
import com.foxminded.dao.*;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.generator.CourseGenerator;
import com.foxminded.generator.GroupGenerator;
import com.foxminded.generator.StudentGenerator;
import com.foxminded.reader.ResourceFileReader;
import com.foxminded.view.ConsoleMenu;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Disabled("Explicitly Disabled")
class ConsoleMenuIntegrationTest {
    private static final Random RANDOM = new Random(10);
    private static final int COUNT_OF_GROUPS = 10;
    private static final int COUNT_OF_COURSES = 10;
    private static final int COUNT_OF_STUDENTS = 200;
    private static final ByteArrayOutputStream OUTPUT = new ByteArrayOutputStream();

    private static Connection connection;
    private static DBFactory dbFactory;

    @Mock
    private ConsoleReader mockConsoleReader;

    @BeforeAll
    private static void createTables() throws DaoException {
        ConnectionProvider connectionProvider = new PropertyConnectionProvider();
        dbFactory = new DBFactory(connectionProvider);
        System.out.println("try create table...");
        dbFactory.createTables();
        connection = connectionProvider.getConnection();
        System.out.println("Connection to H2 open");

        StudentDao studentDao = new StudentDaoImpl(connectionProvider);
        GroupDao groupDao = new GroupDaoImpl(connectionProvider, studentDao);
        CourseDao courseDao = new CourseDaoImpl(connectionProvider);

        //GroupGenerator and save
        List<Group> groupList = new GroupGenerator(RANDOM).generate(COUNT_OF_GROUPS);
        groupDao.saveAll(groupList);

        //CourseGenerator and save
        List<Course> courseList = new CourseGenerator(new ResourceFileReader()).generate(COUNT_OF_COURSES);
        courseDao.saveAll(courseList);

        //StudentGenerator and save
        List<Student> studentList = new StudentGenerator(new ResourceFileReader(), RANDOM).generate(COUNT_OF_STUDENTS);

        GroupAssigner groupAssigner = new GroupAssigner(RANDOM);
        //List<Group> assignedGroupList = groupAssigner.assign(groupList, studentList);
        groupAssigner.assign(groupList, studentList);

        Assigner<Student, Course> courseAssigner = new CourseAssigner(RANDOM);
        List<Student> assignedStudentList = courseAssigner.assign(studentList, courseList);
        studentDao.saveAll(assignedStudentList);

        System.setOut(new PrintStream(OUTPUT));
    }

    @AfterAll
    private static void dropTables() throws SQLException {
        dbFactory.dropTables();
        connection.close();
        System.out.println("Connection to H2 close");
        System.setOut(null);
    }

    @BeforeEach
    void setUp()  {
        when(mockConsoleReader.readString()).thenReturn("a", "b", "English", "c", "NewFirstName", "NewLastName", "d", "e", "f", "q");
        when(mockConsoleReader.readInt()).thenReturn(15, 101, 1, 201, 155, 1, 155, 1);
    }

    @Test
    void main() {
        String expected = buildExpected();

        ConsoleMenu consoleMenu = new ConsoleMenu(mockConsoleReader);
        consoleMenu.readFromConsole();

        assertEquals(expected, OUTPUT.toString());
    }

    private String buildExpected() {
        StringJoiner expected = new StringJoiner(System.lineSeparator(), "", System.lineSeparator());
        expected.add("Find all groups with less or equals students count")
                .add("enter count of student in group:")
                .add("Connection OK")
                .add("find groups OK...")
                .add("Group{id=105, name='CN-38'}")
                .add("Group{id=110, name='AY-53'}")
                .add("Chose an action and press the button and press ENTER")
                .add("a. Find all groups with less or equals students count")
                .add("b. Find all students related to course with given name")
                .add("c. Add new student")
                .add("d. Delete student by STUDENT_ID")
                .add("e. Add a student to the course (from a list)")
                .add("f. Remove the student from one of his or her courses")
                .add("q. exit")
                .add("Find all students related to course with given name")
                .add("enter course name:")
                .add("Connection OK")
                .add("find students by course name OK...")
                .add("Student{id=4, groupId=105, firstName='Maxim', lastName='Noskov', courses=[Course{id=3, name='History', description='this is History'}, Course{id=8, name='English', description='this is English'}, Course{id=10, name='Psychology', description='this is Psychology'}]}")
                .add("Student{id=11, groupId=103, firstName='Ivan', lastName='Noskov', courses=[Course{id=6, name='Physics', description='this is Physics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=18, groupId=109, firstName='Artem', lastName='Sokolov', courses=[Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}, Course{id=2, name='Chemistry', description='this is Chemistry'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=21, groupId=101, firstName='Denis', lastName='Sobolev', courses=[Course{id=2, name='Chemistry', description='this is Chemistry'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=28, groupId=101, firstName='Egor', lastName='Gusev', courses=[Course{id=4, name='Economics', description='this is Economics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=32, groupId=102, firstName='Aleksandr', lastName='Sobolev', courses=[Course{id=4, name='Economics', description='this is Economics'}, Course{id=7, name='Informatics', description='this is Informatics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=35, groupId=106, firstName='Artem', lastName='Sokolov', courses=[Course{id=2, name='Chemistry', description='this is Chemistry'}, Course{id=5, name='Philosophy', description='this is Philosophy'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=36, groupId=103, firstName='Aleksandr', lastName='Nikitin', courses=[Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}, Course{id=8, name='English', description='this is English'}, Course{id=9, name='Biology', description='this is Biology'}]}")
                .add("Student{id=38, groupId=104, firstName='Stepan', lastName='Noskov', courses=[Course{id=7, name='Informatics', description='this is Informatics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=41, groupId=101, firstName='Maxim', lastName='Orehov', courses=[Course{id=8, name='English', description='this is English'}, Course{id=9, name='Biology', description='this is Biology'}]}")
                .add("Student{id=45, groupId=104, firstName='Daniil', lastName='Petrov', courses=[Course{id=8, name='English', description='this is English'}, Course{id=9, name='Biology', description='this is Biology'}]}")
                .add("Student{id=49, groupId=105, firstName='Daniil', lastName='Noskov', courses=[Course{id=8, name='English', description='this is English'}, Course{id=9, name='Biology', description='this is Biology'}]}")
                .add("Student{id=55, groupId=107, firstName='Sergei', lastName='Sokolov', courses=[Course{id=4, name='Economics', description='this is Economics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=65, groupId=103, firstName='Artem', lastName='Belov', courses=[Course{id=6, name='Physics', description='this is Physics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=73, groupId=102, firstName='Ivan', lastName='Zaitcev', courses=[Course{id=6, name='Physics', description='this is Physics'}, Course{id=7, name='Informatics', description='this is Informatics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=77, groupId=109, firstName='Evgenii', lastName='Sobolev', courses=[Course{id=2, name='Chemistry', description='this is Chemistry'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=85, groupId=103, firstName='Pavel', lastName='Kabanov', courses=[Course{id=3, name='History', description='this is History'}, Course{id=5, name='Philosophy', description='this is Philosophy'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=90, groupId=107, firstName='Sergei', lastName='Semenov', courses=[Course{id=5, name='Philosophy', description='this is Philosophy'}, Course{id=6, name='Physics', description='this is Physics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=110, groupId=109, firstName='Aleksandr', lastName='Ivanov', courses=[Course{id=8, name='English', description='this is English'}, Course{id=9, name='Biology', description='this is Biology'}]}")
                .add("Student{id=112, groupId=103, firstName='Evgenii', lastName='Popov', courses=[Course{id=3, name='History', description='this is History'}, Course{id=5, name='Philosophy', description='this is Philosophy'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=117, groupId=107, firstName='Evgenii', lastName='Noskov', courses=[Course{id=7, name='Informatics', description='this is Informatics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=123, groupId=105, firstName='Petr', lastName='Tihonov', courses=[Course{id=7, name='Informatics', description='this is Informatics'}, Course{id=8, name='English', description='this is English'}, Course{id=9, name='Biology', description='this is Biology'}]}")
                .add("Student{id=124, groupId=103, firstName='Ivan', lastName='Lapin', courses=[Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}, Course{id=3, name='History', description='this is History'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=125, groupId=108, firstName='Dmitriy', lastName='Noskov', courses=[Course{id=2, name='Chemistry', description='this is Chemistry'}, Course{id=3, name='History', description='this is History'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=128, groupId=103, firstName='Igor', lastName='Kabanov', courses=[Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}, Course{id=8, name='English', description='this is English'}, Course{id=10, name='Psychology', description='this is Psychology'}]}")
                .add("Student{id=129, groupId=107, firstName='Evgenii', lastName='Ivanov', courses=[Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}, Course{id=8, name='English', description='this is English'}, Course{id=10, name='Psychology', description='this is Psychology'}]}")
                .add("Student{id=135, groupId=101, firstName='Sergei', lastName='Noskov', courses=[Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=136, groupId=107, firstName='Igor', lastName='Ivanov', courses=[Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=171, groupId=104, firstName='Denis', lastName='Sokolov', courses=[Course{id=4, name='Economics', description='this is Economics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=175, groupId=104, firstName='Petr', lastName='Lapin', courses=[Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}, Course{id=4, name='Economics', description='this is Economics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=176, groupId=103, firstName='Sergei', lastName='Popov', courses=[Course{id=6, name='Physics', description='this is Physics'}, Course{id=8, name='English', description='this is English'}, Course{id=9, name='Biology', description='this is Biology'}]}")
                .add("Student{id=178, groupId=102, firstName='Roman', lastName='Volkov', courses=[Course{id=5, name='Philosophy', description='this is Philosophy'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=182, groupId=105, firstName='Stepan', lastName='Morozov', courses=[Course{id=7, name='Informatics', description='this is Informatics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=185, groupId=102, firstName='Pavel', lastName='Petrov', courses=[Course{id=4, name='Economics', description='this is Economics'}, Course{id=7, name='Informatics', description='this is Informatics'}, Course{id=8, name='English', description='this is English'}]}")
                .add("Student{id=192, groupId=105, firstName='Egor', lastName='Sokolov', courses=[Course{id=8, name='English', description='this is English'}]}")
                .add("Chose an action and press the button and press ENTER")
                .add("a. Find all groups with less or equals students count")
                .add("b. Find all students related to course with given name")
                .add("c. Add new student")
                .add("d. Delete student by STUDENT_ID")
                .add("e. Add a student to the course (from a list)")
                .add("f. Remove the student from one of his or her courses")
                .add("q. exit")
                .add("Add new student")
                .add("enter first name:")
                .add("enter last name:")
                .add("Connection OK")
                .add("GET ALL group OK...")
                .add("Group{id=101, name='PG-30'}")
                .add("Group{id=102, name='ES-78'}")
                .add("Group{id=103, name='VC-39'}")
                .add("Group{id=104, name='DK-50'}")
                .add("Group{id=105, name='CN-38'}")
                .add("Group{id=106, name='VD-58'}")
                .add("Group{id=107, name='RL-40'}")
                .add("Group{id=108, name='SA-82'}")
                .add("Group{id=109, name='EW-75'}")
                .add("Group{id=110, name='AY-53'}")
                .add("enter group id from list:")
                .add("Connection OK")
                .add("GET ALL courses OK...")
                .add("Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}")
                .add("Course{id=2, name='Chemistry', description='this is Chemistry'}")
                .add("Course{id=3, name='History', description='this is History'}")
                .add("Course{id=4, name='Economics', description='this is Economics'}")
                .add("Course{id=5, name='Philosophy', description='this is Philosophy'}")
                .add("Course{id=6, name='Physics', description='this is Physics'}")
                .add("Course{id=7, name='Informatics', description='this is Informatics'}")
                .add("Course{id=8, name='English', description='this is English'}")
                .add("Course{id=9, name='Biology', description='this is Biology'}")
                .add("Course{id=10, name='Psychology', description='this is Psychology'}")
                .add("enter course id from list:")
                .add("Connection OK")
                .add("GET BY ID OK... course with id 1")
                .add("Connection OK")
                .add("Connection OK")
                .add("SAVE OK... student with lastName NewLastName")
                .add("Connection OK")
                .add("All batches ok - students_courses SAVE")
                .add("successfully add student:")
                .add("Student{id=201, groupId=101, firstName='NewFirstName', lastName='NewLastName', courses=[Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}]}")
                .add("Chose an action and press the button and press ENTER")
                .add("a. Find all groups with less or equals students count")
                .add("b. Find all students related to course with given name")
                .add("c. Add new student")
                .add("d. Delete student by STUDENT_ID")
                .add("e. Add a student to the course (from a list)")
                .add("f. Remove the student from one of his or her courses")
                .add("q. exit")
                .add("delete student by id")
                .add("enter student id:")
                .add("Connection OK")
                .add("GET BY id OK... student with id 201")
                .add("Connection OK")
                .add("DELETE OK... student with id 201")
                .add("successfully delete student:")
                .add("Student{id=201, groupId=101, firstName='NewFirstName', lastName='NewLastName', courses=[Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}]}")
                .add("Chose an action and press the button and press ENTER")
                .add("a. Find all groups with less or equals students count")
                .add("b. Find all students related to course with given name")
                .add("c. Add new student")
                .add("d. Delete student by STUDENT_ID")
                .add("e. Add a student to the course (from a list)")
                .add("f. Remove the student from one of his or her courses")
                .add("q. exit")
                .add("Add a student to the course (from a list)")
                .add("enter student id:")
                .add("Connection OK")
                .add("GET BY id OK... student with id 155")
                .add("Connection OK")
                .add("GET ALL courses OK...")
                .add("Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}")
                .add("Course{id=2, name='Chemistry', description='this is Chemistry'}")
                .add("Course{id=3, name='History', description='this is History'}")
                .add("Course{id=4, name='Economics', description='this is Economics'}")
                .add("Course{id=5, name='Philosophy', description='this is Philosophy'}")
                .add("Course{id=6, name='Physics', description='this is Physics'}")
                .add("Course{id=7, name='Informatics', description='this is Informatics'}")
                .add("Course{id=8, name='English', description='this is English'}")
                .add("Course{id=9, name='Biology', description='this is Biology'}")
                .add("Course{id=10, name='Psychology', description='this is Psychology'}")
                .add("enter course id from list:")
                .add("Connection OK")
                .add("GET BY ID OK... course with id 1")
                .add("Connection OK")
                .add("UPDATE OK... student with id 155")
                .add("Connection OK")
                .add("Remove OK...")
                .add("Connection OK")
                .add("All batches ok - students_courses SAVE")
                .add("Student{id=155, groupId=107, firstName='Aleksei', lastName='Tihonov', courses=[Course{id=4, name='Economics', description='this is Economics'}, Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}]}")
                .add("successfully add to the course Mathematical analysis")
                .add("Chose an action and press the button and press ENTER")
                .add("a. Find all groups with less or equals students count")
                .add("b. Find all students related to course with given name")
                .add("c. Add new student")
                .add("d. Delete student by STUDENT_ID")
                .add("e. Add a student to the course (from a list)")
                .add("f. Remove the student from one of his or her courses")
                .add("q. exit")
                .add("Remove student from the course")
                .add("enter student id:")
                .add("Connection OK")
                .add("GET BY id OK... student with id 155")
                .add("select course for remove:")
                .add("Course{id=1, name='Mathematical analysis', description='this is Mathematical analysis'}")
                .add("Course{id=4, name='Economics', description='this is Economics'}")
                .add("enter course id from list:")
                .add("Connection OK")
                .add("GET BY ID OK... course with id 1")
                .add("Connection OK")
                .add("UPDATE OK... student with id 155")
                .add("Connection OK")
                .add("Remove OK...")
                .add("Connection OK")
                .add("All batches ok - students_courses SAVE")
                .add("Student{id=155, groupId=107, firstName='Aleksei', lastName='Tihonov', courses=[Course{id=4, name='Economics', description='this is Economics'}]}")
                .add("successfully remove from course Mathematical analysis")
                .add("Chose an action and press the button and press ENTER")
                .add("a. Find all groups with less or equals students count")
                .add("b. Find all students related to course with given name")
                .add("c. Add new student")
                .add("d. Delete student by STUDENT_ID")
                .add("e. Add a student to the course (from a list)")
                .add("f. Remove the student from one of his or her courses")
                .add("q. exit");
        return expected.toString();
    }

}
