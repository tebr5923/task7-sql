package com.foxminded.view;

import com.foxminded.dao.*;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SuppressWarnings("squid:S106")//Console application
public class Console {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final StudentDao STUDENT_DAO =
            new StudentDaoImpl(new PropertyConnectionProvider());
    private static final GroupDao GROUP_DAO =
            new GroupDaoImpl(new PropertyConnectionProvider(), new StudentDaoImpl(new PropertyConnectionProvider()));
    private static final CourseDao COURSE_DAO =
            new CourseDaoImpl(new PropertyConnectionProvider());

    public void showMainMenu() {
        String mainString = "Chose an action and press the button and press ENTER";
        String menuItemA = "a. Find all groups with less or equals students count";
        String menuItemB = "b. Find all students related to course with given name";
        String menuItemC = "c. Add new student";
        String menuItemD = "d. Delete student by STUDENT_ID";
        String menuItemE = "e. Add a student to the course (from a list)";
        String menuItemF = "f. Remove the student from one of his or her courses";
        String menuExit = "q. exit";

        System.out.println(mainString);
        System.out.println(menuItemA);
        System.out.println(menuItemB);
        System.out.println(menuItemC);
        System.out.println(menuItemD);
        System.out.println(menuItemE);
        System.out.println(menuItemF);
        System.out.println(menuExit);
    }

    public void readFromConsole() {
        String s = SCANNER.nextLine();
        while (!s.equals("q")) {
            switch (s) {
                case ("a"):
                    findAllGroupsWithLessOrEqualsStudentsCount();
                    break;
                case ("b"):
                    findAllStudentsRelatedToCourse();
                    break;
                case ("c"):
                    addNewStudent();
                    break;
                case ("d"):
                    deleteStudentById();
                    break;
                case ("e"):
                    addStudentToTheCourse();
                    break;
                case ("f"):
                    removeStudentFromCourse();
                    break;
                default:
                    noAction();
                    break;
            }
            showMainMenu();
            s = SCANNER.nextLine();
        }
        SCANNER.close();
    }

    //action a
    private void findAllGroupsWithLessOrEqualsStudentsCount() {
        System.out.println("Find all groups with less or equals students count");
        System.out.println("enter count of student in group:");
        int i = SCANNER.nextInt();
        SCANNER.nextLine(); //?
        try {
            List<Group> groupList = GROUP_DAO.findByStudentsCount(i);
            groupList.forEach(System.out::println);
        } catch (DaoException e) {
            e.printStackTrace();
            //todo: i don't sure about RuntimeDaoException in this place
            // maybe some else RuntimeException?
            throw new RuntimeDaoException("find groups ERROR", e);
        }
    }

    //action b
    private void findAllStudentsRelatedToCourse() {
        System.out.println("Find all students related to course with given name");
        System.out.println("enter course name:");
        String s = SCANNER.nextLine();
        try {
            List<Student> studentList = STUDENT_DAO.findStudentsByCourseName(s);
            studentList.forEach(System.out::println);
        } catch (DaoException e) {
            e.printStackTrace();
            //todo: i don't sure about RuntimeDaoException in this place
            throw new RuntimeDaoException("find students ERROR", e);
        }
    }

    //action c
    private void addNewStudent() {
        System.out.println("Add new student");
        Student student = new Student();
        student.setFirstName(scanFirstName());
        student.setLastName(scanLastName());
        student.setGroupId(scanGroupId());
        soutAllCourses();
        Optional<Course> optionalCourse = scanCourse();
        SCANNER.nextLine(); //?
        student.setCourses(Collections.singletonList(optionalCourse.orElseThrow(() -> new IllegalArgumentException("course not found"))));
        saveStudent(student);
    }

    //action d
    private void deleteStudentById() {
        System.out.println("delete student by id");
        System.out.println("enter student id:");
        int studentId = SCANNER.nextInt();
        SCANNER.nextLine(); //?
        try {
            Student student = STUDENT_DAO.getById(studentId).orElseThrow(() -> new IllegalArgumentException("student not found"));
            STUDENT_DAO.delete(student);
            System.out.println("successfully delete student:");
            System.out.println(student);
        } catch (DaoException e) {
            e.printStackTrace();
            //todo: i don't sure about RuntimeDaoException in this place
            throw new RuntimeDaoException("student delete ERROR", e);
        }
    }

    //action e
    private void addStudentToTheCourse() {
        System.out.println("Add a student to the course (from a list)");
        Student student = scanStudent().orElseThrow(() -> new IllegalArgumentException("student not found"));
        soutAllCourses();
        Course course = scanCourse().orElseThrow(() -> new IllegalArgumentException("course not found"));
        SCANNER.nextLine(); //?
        List<Course> courseList = student.getCourses();
        if (!courseList.contains(course)) {
            courseList.add(course);
            student.setCourses(courseList);
            try {
                STUDENT_DAO.update(student);
                System.out.println(student);
                System.out.println("successfully add to the course " + course.getName());
            } catch (DaoException e) {
                e.printStackTrace();
                //todo: i don't sure about RuntimeDaoException in this place
                throw new RuntimeDaoException("don't update student", e);
            }
        } else {
            System.out.println("this student already added to this course");
        }
    }

    //action f
    private void removeStudentFromCourse() {
        System.out.println("Remove student from the course");
        Student student = scanStudent().orElseThrow(() -> new IllegalArgumentException("student not found"));
        System.out.println("select course for remove:");
        List<Course> courseList = student.getCourses();
        courseList.forEach(System.out::println);
        Course course = scanCourse().orElseThrow(() -> new IllegalArgumentException("course not found"));
        SCANNER.nextLine(); //?
        if (courseList.contains(course)) {
            courseList.remove(course);
            student.setCourses(courseList);
            try {
                STUDENT_DAO.update(student);
                System.out.println(student);
                System.out.println("successfully remove from course " + course.getName());
            } catch (DaoException e) {
                e.printStackTrace();
                //todo: i don't sure about RuntimeDaoException in this place
                throw new RuntimeDaoException("don't update student", e);
            }
        } else {
            System.out.println("can't remove, this student not related to this course");
        }
    }

    //no action
    private void noAction() {
        System.out.println("no action on your enter");
    }

    private Optional<Student> scanStudent() {
        System.out.println("enter student id:");
        Optional<Student> optionalStudent = Optional.empty();
        try {
            optionalStudent = STUDENT_DAO.getById(SCANNER.nextInt());
        } catch (DaoException e) {
            e.printStackTrace();
            //todo: i don't sure about RuntimeDaoException in this place
            throw new RuntimeDaoException("don't load student", e);
        }
        return optionalStudent;
    }

    private String scanFirstName() {
        System.out.println("enter first name:");
        return SCANNER.nextLine();
    }

    private String scanLastName() {
        System.out.println("enter last name:");
        return SCANNER.nextLine();
    }

    private int scanGroupId() {
        try {
            GROUP_DAO.getAll().forEach(System.out::println);
        } catch (DaoException e) {
            e.printStackTrace();
            //todo: i don't sure about RuntimeDaoException in this place
            throw new RuntimeDaoException("don't load groups", e);
        }
        System.out.println("enter group id from list:");
        return SCANNER.nextInt();
    }

    private void soutAllCourses(){
        try {
            COURSE_DAO.getAll().forEach(System.out::println);
        } catch (DaoException e) {
            e.printStackTrace();
            //todo: i don't sure about RuntimeDaoException in this place
            throw new RuntimeDaoException("don't load courses", e);
        }
    }

    private Optional<Course> scanCourse() {
        Optional<Course> optionalCourse = Optional.empty();
        System.out.println("enter course id from list:");
        try {
            optionalCourse = COURSE_DAO.getById(SCANNER.nextInt());
        } catch (DaoException e) {
            e.printStackTrace();
            //todo: i don't sure about RuntimeDaoException in this place
            throw new RuntimeDaoException("don't load course", e);
        }
        return optionalCourse;
    }

    private void saveStudent(Student student) {
        try {
            STUDENT_DAO.save(student);
            System.out.println("successfully add student:");
            System.out.println(student);
        } catch (DaoException e) {
            e.printStackTrace();
            //todo: i don't sure about RuntimeDaoException in this place
            throw new RuntimeDaoException("don't save student", e);
        }
    }
}
