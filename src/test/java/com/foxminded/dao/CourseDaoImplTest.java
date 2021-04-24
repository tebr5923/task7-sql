package com.foxminded.dao;

import com.foxminded.domain.Course;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseDaoImplTest extends AbstractDaoTest {

    @Test
    void getByName_shouldReturnCourse_whenGetCourseWhichExist() throws DaoException {
        Course expected = math;

        Optional<Course> actual = courseDao.getByName(math.getName());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getByName_shouldReturnEmptyCourse_whenGetCourseWhichNotExist() throws DaoException {
        Optional<Course> actual = courseDao.getByName("ntExt");

        assertFalse(actual.isPresent());
    }

    @Test
    void getById_shouldReturnCourse_whenGetCourseWhichExist() throws DaoException {
        Course expected = math;

        Optional<Course> actual = courseDao.getById(math.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getById_shouldReturnEmptyCourse_whenGetCourseWhichNotExist() throws DaoException {
        Optional<Course> actual = courseDao.getById(999);

        assertFalse(actual.isPresent());
    }

    @Test
    void getAll_shouldReturnAllCourses() throws DaoException {
        List<Course> expected = Arrays.asList(history, math, economics);

        List<Course> actual = courseDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void save_shouldSaveCourse_whenSavingCourseNotExist() throws DaoException {
        Course expected = new Course();
        expected.setName("newCourse");
        expected.setDescription("new course description");
        courseDao.save(expected);

        Optional<Course> actual = courseDao.getByName("newCourse");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void save_shouldThrowDaoException_whenSavingCourseExist() {
        assertThrows(DaoException.class, () -> courseDao.save(math));
    }

    @Test
    void update_shouldUpdateCourse_whenUpdatingCourseExist() throws DaoException {
        Course expected = new Course();
        expected.setId(1);
        expected.setName("updCourse");
        expected.setDescription("this is updCourse");
        courseDao.update(expected);

        Optional<Course> actual = courseDao.getById(1);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void update_shouldThrowDaoException_whenUpdatingCourseNotExist() throws DaoException {
        Course course = new Course();
        course.setId(558);
        course.setName("updCourse");
        course.setDescription("this is updCourse");

        Optional<Course> actual = courseDao.getById(558);
        assertFalse(actual.isPresent());

        assertThrows(DaoException.class, () -> courseDao.update(course));
    }

    @Test
    void delete_shouldDeleteCourse_whenDeletingCourseExist() throws DaoException {
        Optional<Course> actual = courseDao.getById(math.getId());
        assertTrue(actual.isPresent());

        courseDao.delete(math);

        actual = courseDao.getById(math.getId());
        assertFalse(actual.isPresent());
    }

    @Test
    void delete_shouldThrowDaoException_whenDeletingCourseNotExist() throws DaoException {
        Course course = new Course();
        course.setId(558);

        Optional<Course> actual = courseDao.getById(course.getId());
        assertFalse(actual.isPresent());

        assertThrows(DaoException.class, () -> courseDao.delete(course));
    }
}
