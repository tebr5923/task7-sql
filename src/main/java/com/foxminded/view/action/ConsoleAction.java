package com.foxminded.view.action;

import com.foxminded.console_reader.ConsoleReader;
import com.foxminded.dao.ConnectionProvider;
import com.foxminded.dao.CourseDao;
import com.foxminded.dao.CourseDaoImpl;
import com.foxminded.dao.DaoException;
import com.foxminded.dao.GroupDao;
import com.foxminded.dao.GroupDaoImpl;
import com.foxminded.dao.PropertyConnectionProvider;
import com.foxminded.dao.RuntimeDaoException;
import com.foxminded.dao.StudentDao;
import com.foxminded.dao.StudentDaoImpl;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("squid:S106")//Console action
public class ConsoleAction implements Action {
    private static final ConnectionProvider CONNECTION_PROVIDER =
            new PropertyConnectionProvider();
    private static final StudentDao STUDENT_DAO =
            new StudentDaoImpl(CONNECTION_PROVIDER);
    private static final GroupDao GROUP_DAO =
            new GroupDaoImpl(CONNECTION_PROVIDER, STUDENT_DAO);
    private static final CourseDao COURSE_DAO =
            new CourseDaoImpl(CONNECTION_PROVIDER);

    private final ConsoleReader consoleReader;

    public ConsoleAction(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
    }

    @Override
    public void findAllGroupsWithLessOrEqualsStudentsCount() {
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

    @Override
    public void findAllStudentsRelatedToCourse() {
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

    @Override
    public void addNewStudent() {
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

    @Override
    public void deleteStudentById() {
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

    @Override
    public void addStudentToTheCourse() {
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

    @Override
    public void removeStudentFromCourse() {
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

    @Override
    public void noAction() {
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
