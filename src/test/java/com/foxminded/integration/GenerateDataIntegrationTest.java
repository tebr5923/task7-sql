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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerateDataIntegrationTest {
    private static final int COUNT_OF_GROUPS = 10;
    private static final int COUNT_OF_COURSES = 10;
    private static final int COUNT_OF_STUDENTS = 200;
    private static final String PATTERN_OF_GROUP_NAME = "[A-Z]{2}-\\d{2}";
    private static final String PATTERN_OF_COURSE_NAME = "[A-Z][a-z]+\\s?[a-z]*";
    private static final String PATTERN_OF_STUDENT_NAME = "[A-Z][a-z]+";

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
        List<Group> groupList = new GroupGenerator().generate(COUNT_OF_GROUPS);
        groupDao.saveAll(groupList);

        //CourseGenerator and save
        List<Course> courseList = new CourseGenerator(new ResourceFileReader()).generate(COUNT_OF_COURSES);
        courseDao.saveAll(courseList);

        //StudentGenerator and save
        List<Student> studentList = new StudentGenerator(new ResourceFileReader()).generate(COUNT_OF_STUDENTS);

        GroupAssigner groupAssigner = new GroupAssigner();
        List<Group> assignedGroupList = groupAssigner.assign(groupList, studentList);

        Assigner<Student, Course> courseAssigner = new CourseAssigner();
        List<Student> assignedStudentList = courseAssigner.assign(studentList, courseList);
        studentDao.saveAll(assignedStudentList);

    }

    @AfterAll
    private static void dropTables() throws SQLException {
        dbFactory.dropTables();
        connection.close();
        System.out.println("Connection to H2 close");
    }

    //group
    @Test
    void getAllGroups_shouldReturnAllGroups_withCountOfGroupsIsEqualCOUNT_OF_GROUPS() throws DaoException {
        int actual = groupDao.getAll().size();

        assertEquals(COUNT_OF_GROUPS, actual);
    }

    @Test
    void getAllGroups_shouldReturnAllGroups_withPatternOfNameIsEqualPATTERN_OF_GROUP_NAME() throws DaoException {
        List<Group> groupList = groupDao.getAll();

        groupList.forEach(group -> assertTrue(Pattern.matches(PATTERN_OF_GROUP_NAME, group.getName())));
    }

    @Test
    void getStudentsByGroup_shouldReturnAllStudentsOfFGroup_withMoreTenAndLessThirtyStudents() throws DaoException {
        boolean actual = true;
        List<Group> groupList = groupDao.getAll();
        for (Group group : groupList) {
            List<Student> studentList = group.getStudents();
            if (!studentList.isEmpty()) {
                if (studentList.size() < 10 || studentList.size() > 30) {
                    actual = false;
                }
            }
        }
        assertTrue(actual);
    }

    //course
    @Test
    void getAllCourses_shouldReturnAllCourses_withCountOfCoursesIsEqualCOUNT_OF_COURSES() throws DaoException {
        int actual = courseDao.getAll().size();

        assertEquals(COUNT_OF_COURSES, actual);
    }

    @Test
    void getAllCourses_shouldReturnAllCourses_withPatternOfNameIsEqualPATTERN_OF_COURSE_NAME() throws DaoException {
        List<Course> courseList = courseDao.getAll();

        courseList.forEach(course -> assertTrue(Pattern.matches(PATTERN_OF_COURSE_NAME, course.getName())));
    }

    //students
    @Test
    void getAllStudents_shouldReturnAllStudents_withCountOfStudentsIsEqualCOUNT_OF_STUDENTS() throws DaoException {
        int actual = studentDao.getAll().size();

        assertEquals(COUNT_OF_STUDENTS, actual);
    }

    @Test
    void getAllStudents_shouldReturnAllStudents_withPatternOfNameIsEqualPATTERN_OF_STUDENT_NAME() throws DaoException {
        List<Student> studentList = studentDao.getAll();

        studentList.forEach(student -> assertTrue(Pattern.matches(PATTERN_OF_STUDENT_NAME, student.getFirstName())));
        studentList.forEach(student -> assertTrue(Pattern.matches(PATTERN_OF_STUDENT_NAME, student.getLastName())));
    }

    @Test
    void getCoursesByStudents_shouldReturnAllCoursesOfStudent_withCountOfCoursesThreeOrLess() throws DaoException {
        List<Student> studentList = studentDao.getAll();

        assertEquals(COUNT_OF_STUDENTS, studentList.size());
        studentList.forEach(student -> assertTrue(student.getCourses().size() <= 3));
    }

    @Test
    void getCoursesByStudents_shouldReturnAllCoursesOfStudent_withCountOfCoursesOneOrMore() throws DaoException {
        List<Student> studentList = studentDao.getAll();

        studentList.forEach(student -> assertTrue(student.getCourses().size() >= 1));
    }

}
