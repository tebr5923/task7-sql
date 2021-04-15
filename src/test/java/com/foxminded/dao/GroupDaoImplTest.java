package com.foxminded.dao;

import com.foxminded.domain.Group;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoImplTest {
    private static Connection connection;
    private static DBFactory dbFactory;

    private GroupDaoImpl groupDao;

    @BeforeAll
    public static void createTables() {
        dbFactory = new DBFactory(new ConnectionFactory());
        System.out.println("try create table...");
        dbFactory.createTables();
        connection = new ConnectionFactory().getConnection();
        System.out.println("Connection to H2 open");
    }

    @AfterAll
    public static void dropTables() throws SQLException {
        dbFactory.dropTables();
        connection.close();
        System.out.println("Connection to H2 close");
    }

    @BeforeEach
    void setUp() {
        groupDao = new GroupDaoImpl(new ConnectionFactory());
        String sql = "INSERT INTO groups (name) values('save1');\n" +
                "INSERT INTO groups (name) values('save2');";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("execute query for test data save");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("WRONG QUERY");
        }
    }

    @AfterEach
    void tearDown() {
        String sql = "DELETE FROM groups;ALTER SEQUENCE groups_id_seq RESTART WITH 101;";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("execute query for clear test data");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("WRONG QUERY");
        }
    }

    @Test
    void getByName_shouldReturnGroup_whenGetGroupWhichExist() throws DaoException {
        Group expected = new Group();
        expected.setId(102);
        expected.setName("save2");

        Optional<Group> actual = groupDao.getByName("save2");

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
        Group expected = new Group();
        expected.setId(102);
        expected.setName("save2");

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
        Group group1 = new Group();
        group1.setId(101);
        group1.setName("save1");
        Group group2 = new Group();
        group2.setId(102);
        group2.setName("save2");
        List<Group> expected = Arrays.asList(group1, group2);

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