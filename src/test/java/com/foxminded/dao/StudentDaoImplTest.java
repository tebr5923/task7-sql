package com.foxminded.dao;

import com.foxminded.domain.Student;
import org.junit.jupiter.api.*;

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
