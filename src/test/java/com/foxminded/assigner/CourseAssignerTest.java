package com.foxminded.assigner;

import com.foxminded.domain.Course;
import com.foxminded.domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseAssignerTest {
    private static final long seed = 8;

    private static List<Student> studentList;
    private static List<Course> courseList;
    private static Student assignedIvanov;
    private static Student assignedPetrov;
    private static Student assignedRomanov;

    @BeforeAll
    private static void createTestData(){
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

        Course history = new Course();
        history.setId(1);
        history.setName("History");
        history.setDescription("this is history");
        Course math = new Course();
        math.setId(2);
        math.setName("Math");
        math.setDescription("this is math");
        Course economics = new Course();
        economics.setId(3);
        economics.setName("Economics");
        economics.setDescription("this is economics");
        Course philosophy = new Course();
        philosophy.setId(4);
        philosophy.setName("Philosophy");
        philosophy.setDescription("this is philosophy");
        courseList = Arrays.asList(history, math, economics, philosophy);

        assignedIvanov = new Student();
        assignedIvanov.setId(1);
        assignedIvanov.setGroupId(101);
        assignedIvanov.setFirstName("Ivan");
        assignedIvanov.setLastName("Ivanov");
        assignedIvanov.setCourses(Arrays.asList(philosophy, math));
        assignedPetrov = new Student();
        assignedPetrov.setId(2);
        assignedPetrov.setGroupId(101);
        assignedPetrov.setFirstName("Petr");
        assignedPetrov.setLastName("Petrov");
        assignedPetrov.setCourses(Arrays.asList(history, philosophy));
        assignedRomanov = new Student();
        assignedRomanov.setId(3);
        assignedRomanov.setGroupId(101);
        assignedRomanov.setFirstName("Roman");
        assignedRomanov.setLastName("Romanov");
        assignedRomanov.setCourses(Collections.singletonList(math));
    }

    @Test
    void assign_shouldReturnStudentListWithCourseList() {
        List<Student> expected = Arrays.asList(assignedIvanov, assignedPetrov, assignedRomanov);

        List<Student> actual = new CourseAssigner(new Random(seed)).assign(studentList, courseList);

        assertEquals(expected, actual);
    }
}
