package com.foxminded.dao;

import com.foxminded.domain.Student;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
}
