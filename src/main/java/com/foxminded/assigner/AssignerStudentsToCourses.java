package com.foxminded.assigner;

import com.foxminded.domain.Course;
import com.foxminded.domain.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AssignerStudentsToCourses implements Assigner<Student, Course>{
    private static final int DEFAULT_MIN_COURSES_AT_STUDENT = 1;
    private static final int DEFAULT_MAX_COURSES_AT_STUDENT = 3;

    private final int minCoursesAtStudent;
    private final int maxCoursesAtStudent;

    public AssignerStudentsToCourses() {
        this(DEFAULT_MIN_COURSES_AT_STUDENT, DEFAULT_MAX_COURSES_AT_STUDENT);
    }

    public AssignerStudentsToCourses(int minCoursesAtStudent, int maxCoursesAtStudent) {
        this.minCoursesAtStudent = minCoursesAtStudent;
        this.maxCoursesAtStudent = maxCoursesAtStudent;
    }

    @Override
    public List<Student> assign(List<Student> studentList, List<Course> courseList) {
        List<Student> assignedStudentList = new ArrayList<>();
        for (Student student :
                studentList) {
            student.setCourses(generateAssignedList(courseList));
            assignedStudentList.add(student);
        }
        return assignedStudentList;
    }

    private List<Course> generateAssignedList(List<Course> courseList) {
        List<Course> assignedList = new ArrayList<>();
        List<Course> tempCourseList = new ArrayList<>(courseList);
        int bound = maxCoursesAtStudent - minCoursesAtStudent;
        int size = new Random().nextInt(bound + 1) + minCoursesAtStudent;
        for (int i = 0; i < size; i++) {
            int index = new Random().nextInt(tempCourseList.size() - i);
            assignedList.add(tempCourseList.remove(index));
        }
        return assignedList;
    }
}
