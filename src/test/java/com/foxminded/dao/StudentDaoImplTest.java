package com.foxminded.dao;

import com.foxminded.domain.Course;
import com.foxminded.domain.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentDaoImplTest extends AbstractDaoTest {

    @Test
    void getById_shouldReturnStudent_whenGetStudentWhichExist() throws DaoException {
        Student expected = ivanov;

        Optional<Student> actual = studentDao.getById(1);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getStudentsByGroup_shouldReturnStudents_whenLoadingStudentsIsExist() throws DaoException {
        List<Student> expected = Arrays.asList(ivanov, petrov, romanov);

        List<Student> actual = studentDao.getStudentsByGroup(101);

        assertEquals(expected, actual);
    }

    @Test
    void getAll_shouldReturnAllStudents() throws DaoException {
        List<Student> expected = Arrays.asList(ivanov, petrov, romanov);

        List<Student> actual = studentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void save_shouldSaveStudentWithEmptyCourses_whenSavingStudentNotExist() throws DaoException {
        Student expected = new Student();
        expected.setGroupId(101);
        expected.setFirstName("newFirstName");
        expected.setLastName("newLastName");
        expected.setCourses(new ArrayList<>());
        studentDao.save(expected);

        Optional<Student> actual = studentDao.getById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void save_shouldSaveStudentWithNotEmptyCourses_whenSavingStudentNotExist() throws DaoException {
        Student expected = new Student();
        expected.setGroupId(101);
        expected.setFirstName("newFirstName");
        expected.setLastName("newLastName");
        expected.setCourses(Arrays.asList(history, economics));
        studentDao.save(expected);

        Optional<Student> actual = studentDao.getById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void save_shouldThrowDaoException_whenSavingStudentRegisterOnCourseWhichNotExist() {
        Student expected = new Student();
        expected.setGroupId(101);
        expected.setFirstName("newFirstName");
        expected.setLastName("newLastName");
        Course notExistCourse = new Course();
        notExistCourse.setId(999);
        expected.setCourses(Arrays.asList(notExistCourse, economics));

        assertThrows(DaoException.class, () -> studentDao.save(expected));
    }

    @Test
    void save_shouldThrowDaoException_whenSavingStudentExist() {
        assertThrows(DaoException.class, () -> studentDao.save(ivanov));
    }

    @Test
    void update_shouldUpdateStudent_whenUpdatingStudentExist() throws DaoException {
        Student expected = new Student();
        expected.setId(1);
        expected.setGroupId(101);
        expected.setFirstName("updFirstName");
        expected.setLastName("updLastName");
        expected.setCourses(Arrays.asList(history, math));

        Optional<Student> actual = studentDao.getById(1);
        assertTrue(actual.isPresent());
        studentDao.update(expected);
        actual = studentDao.getById(1);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void update_shouldThrowDaoException_whenUpdatingStudentNotExist() throws DaoException {
        Student student = new Student();
        student.setId(199);
        student.setGroupId(101);
        student.setFirstName("updFirstName");
        student.setLastName("updLastName");
        student.setCourses(Arrays.asList(history, math));

        Optional<Student> actual = studentDao.getById(199);
        assertFalse(actual.isPresent());

        assertThrows(DaoException.class, () -> studentDao.update(student));
    }

    @Test
    void delete_shouldDeleteStudent_whenDeletingStudentIsExist() throws DaoException {
        Optional<Student> actual = studentDao.getById(ivanov.getId());
        assertTrue(actual.isPresent());

        studentDao.delete(ivanov);

        actual = studentDao.getById(ivanov.getId());
        assertFalse(actual.isPresent());
    }

    @Test
    void delete_shouldThrowDaoException_whenDeletingStudentNotExist() throws DaoException {
        Student student = new Student();
        student.setId(558);

        Optional<Student> actual = studentDao.getById(student.getId());
        assertFalse(actual.isPresent());

        assertThrows(DaoException.class, () -> studentDao.delete(student));
    }

}
