package com.foxminded.assigner;

import com.foxminded.domain.Course;
import com.foxminded.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseAssignerTest {
    private static final long seed = 8;

    private List<Student> studentList;
    private List<Course> courseList;
    private Course history;
    private Course math;
    private Course economics;
    private Course philosophy;

    @BeforeEach
    void setUp() {
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
        studentList = Arrays.asList(ivanov, petrov, romanov);

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
        philosophy = new Course();
        philosophy.setId(4);
        philosophy.setName("Philosophy");
        philosophy.setDescription("this is philosophy");
        courseList = Arrays.asList(history, math, economics, philosophy);
    }

    @Test
    void assign_shouldReturnStudentListWithCourseList() {
        Student assignedIvanov = new Student();
        assignedIvanov.setId(1);
        assignedIvanov.setGroupId(101);
        assignedIvanov.setFirstName("Ivan");
        assignedIvanov.setLastName("Ivanov");
        assignedIvanov.setCourses(Arrays.asList(philosophy, math));
        Student assignedPetrov = new Student();
        assignedPetrov.setId(2);
        assignedPetrov.setGroupId(101);
        assignedPetrov.setFirstName("Petr");
        assignedPetrov.setLastName("Petrov");
        assignedPetrov.setCourses(Arrays.asList(history, math));
        Student assignedRomanov = new Student();
        assignedRomanov.setId(3);
        assignedRomanov.setGroupId(101);
        assignedRomanov.setFirstName("Roman");
        assignedRomanov.setLastName("Romanov");
        assignedRomanov.setCourses(Arrays.asList(math));
        List<Student> expected = Arrays.asList(assignedIvanov, assignedPetrov, assignedRomanov);

        List<Student> actual = new CourseAssigner(new Random(seed)).assign(studentList, courseList);

        assertEquals(expected, actual);
    }
}
