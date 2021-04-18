package com.foxminded.dao;

import com.foxminded.domain.Group;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoImplTest extends AbstractDaoTest{

    @Test
    void getByName_shouldReturnGroup_whenGetGroupWhichExist() throws DaoException {
        Group expected = group101;

        Optional<Group> actual = groupDao.getByName("gr101");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getByName_shouldReturnEmptyGroup_whenGetGroupWhichNotExist() throws DaoException {
        Optional<Group> actual = groupDao.getByName("ntExt");

        assertFalse(actual.isPresent());
    }

    @Test
    void getById_shouldReturnGroup_whenGetGroupWhichExist() throws DaoException {
        Group expected = group102;

        Optional<Group> actual = groupDao.getById(102);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getById_shouldReturnEmptyGroup_whenGetGroupWhichNotExist() throws DaoException {
        Optional<Group> actual = groupDao.getById(55);

        assertFalse(actual.isPresent());
    }

    @Test
    void getAll_shouldReturnAllGroups() throws DaoException {
        List<Group> expected = Arrays.asList(group101, group102);

        List<Group> actual = groupDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void save_shouldSaveGroup_whenSavingGroupNotExist() throws DaoException {
        Group expected = new Group();
        expected.setName("new-1");
        groupDao.save(expected);

        Optional<Group> actual = groupDao.getByName("new-1");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void update_shouldUpdateGroup_whenUpdatingGroupExist() throws DaoException {
        Group expected = new Group();
        expected.setId(101);
        expected.setName("upd");
        groupDao.update(expected);

        Optional<Group> actual = groupDao.getById(101);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    /*@Test
    void update_shouldNotUpdateGroup_whenUpdatingGroupNotExist()*/

}