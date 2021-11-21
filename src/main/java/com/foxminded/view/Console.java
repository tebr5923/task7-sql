package com.foxminded.view;

import com.foxminded.console_reader.ConsoleReader;
import com.foxminded.dao.*;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("squid:S106")//Console application
public class Console {
    private static final ConsoleReader CONSOLE_READER = new ConsoleReader();
    private static final StudentDao STUDENT_DAO =
            new StudentDaoImpl(new PropertyConnectionProvider());
    private static final GroupDao GROUP_DAO =
            new GroupDaoImpl(new PropertyConnectionProvider(), new StudentDaoImpl(new PropertyConnectionProvider()));
    private static final CourseDao COURSE_DAO =
            new CourseDaoImpl(new PropertyConnectionProvider());

    private final ConsoleReader consoleReader;

    public Console() {
        this(CONSOLE_READER);
    }

    public Console(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
    }

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
        String s = consoleReader.readString();
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
            s = consoleReader.readString();
        }
        consoleReader.close();
    }

    //action a
    private void findAllGroupsWithLessOrEqualsStudentsCount() {
        System.out.println("Find all groups with less or equals students count");
        System.out.println("enter count of student in group:");
        int i = consoleReader.readInt();
        try {
            List<Group> groupList = GROUP_DAO.findByStudentsCount(i);
            groupList.forEach(System.out::println);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new RuntimeDaoException("find groups ERROR", e);
        }
    }

    //action b
    private void findAllStudentsRelatedToCourse() {
        System.out.println("Find all students related to course with given name");
        System.out.println("enter course name:");
        String s = consoleReader.readString();
        try {
            List<Student> studentList = STUDENT_DAO.findStudentsByCourseName(s);
            studentList.forEach(System.out::println);
        } catch (DaoException e) {
            e.printStackTrace();
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
        student.setCourses(Collections.singletonList(optionalCourse.orElseThrow(() -> new IllegalArgumentException("course not found"))));
        saveStudent(student);
    }

    //action d
    private void deleteStudentById() {
        System.out.println("delete student by id");
        System.out.println("enter student id:");
        int studentId = consoleReader.readInt();
        try {
            Student student = STUDENT_DAO.getById(studentId).orElseThrow(() -> new IllegalArgumentException("student not found"));
            STUDENT_DAO.delete(student);
            System.out.println("successfully delete student:");
            System.out.println(student);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new RuntimeDaoException("student delete ERROR", e);
        }
    }

    //action e
    private void addStudentToTheCourse() {
        System.out.println("Add a student to the course (from a list)");
        Student student = scanStudent().orElseThrow(() -> new IllegalArgumentException("student not found"));
        soutAllCourses();
        Course course = scanCourse().orElseThrow(() -> new IllegalArgumentException("course not found"));
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
        if (courseList.remove(course)) {
            //student.setCourses(courseList); No need as courseList is already a reference to the same object
            try {
                STUDENT_DAO.update(student);
                System.out.println(student);
                System.out.println("successfully remove from course " + course.getName());
            } catch (DaoException e) {
                e.printStackTrace();
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
        try {
            return STUDENT_DAO.getById(consoleReader.readInt());
        } catch (DaoException e) {
            e.printStackTrace();
            throw new RuntimeDaoException("don't load student", e);
        }
    }

    private String scanFirstName() {
        System.out.println("enter first name:");
        return consoleReader.readString();
    }

    private String scanLastName() {
        System.out.println("enter last name:");
        return consoleReader.readString();
    }

    private int scanGroupId() {
        try {
            GROUP_DAO.getAll().forEach(System.out::println);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new RuntimeDaoException("don't load groups", e);
        }
        System.out.println("enter group id from list:");
        return consoleReader.readInt();
    }

    private void soutAllCourses() {
        try {
            COURSE_DAO.getAll().forEach(System.out::println);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new RuntimeDaoException("don't load courses", e);
        }
    }

    private Optional<Course> scanCourse() {
        System.out.println("enter course id from list:");
        try {
            return COURSE_DAO.getById(consoleReader.readInt());
        } catch (DaoException e) {
            e.printStackTrace();
            throw new RuntimeDaoException("don't load course", e);
        }
    }

    private void saveStudent(Student student) {
        try {
            STUDENT_DAO.save(student);
            System.out.println("successfully add student:");
            System.out.println(student);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new RuntimeDaoException("don't save student", e);
        }
    }
}
