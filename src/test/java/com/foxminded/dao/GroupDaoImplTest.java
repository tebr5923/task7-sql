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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoImplTest {
    private static Connection connection;
    private static DBFactory dbFactory;

    private GroupDaoImpl groupDao;

    @BeforeAll
    public static void createTables() {
        //create Tables
        dbFactory = new DBFactory();
        System.out.println("try create table...");
        dbFactory.createTables();
        connection = new DaoFactory().getConnection();
        System.out.println("Connection to H2 open");
    }

    @AfterAll
    public static void dropTables() throws SQLException {
        //drop Tables
        dbFactory.dropTables();
        connection.close();
        System.out.println("Connection to H2 close");
    }

    @BeforeEach
    void setUp() {
        groupDao = new GroupDaoImpl(new DaoFactory());
        String sql = "INSERT INTO groups (name) values('save1');\n" +
                "INSERT INTO groups (name) values('save2');";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("execute query for data save");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("WRONG QUERY");
        }
    }

    @AfterEach
    void tearDown() {
        groupDao = new GroupDaoImpl(new DaoFactory());
        String sql = "DELETE FROM groups;ALTER SEQUENCE groups_id_seq RESTART WITH 101";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("execute query for data save");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("WRONG QUERY");
        }
    }

    @Test
    void getByName_shouldReturnGroup_whenGetGroupWhichExist() {
        Optional<Group> expected = Optional.of(new Group(102, "save2"));

        Optional<Group> actual = groupDao.getByName("save2");

        assertEquals(expected, actual);
    }

    @Test
    void getByName_shouldReturnEmptyGroup_whenGetGroupWhichNotExist() {
        Optional<Group> expected = Optional.empty();

        Optional<Group> actual = groupDao.getByName("ntExt");

        assertEquals(expected, actual);
    }

    @Test
    void getById_shouldReturnGroup_whenGetGroupWhichExist() {
        Optional<Group> expected = Optional.of(new Group(102, "save2"));

        Optional<Group> actual = groupDao.getById(102);

        assertEquals(expected, actual);
    }

    @Test
    void getById_shouldReturnEmptyGroup_whenGetGroupWhichNotExist() {
        Optional<Group> expected = Optional.empty();

        Optional<Group> actual = groupDao.getById(55);

        assertEquals(expected, actual);
    }

    @Test
    void getAll_shouldReturnAllGroups() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(101, "save1"));
        expected.add(new Group(102, "save2"));

        List<Group> actual = groupDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void save_shouldSaveGroup_whenSavingGroupNotExist() {
        Group expected = new Group();
        expected.setName("new-1");
        groupDao.save(expected);

        Group actual = groupDao.getByName("new-1").orElse(new Group(-1, ""));

        assertEquals(expected, actual);
    }

    @Test
    void update_shouldUpdateGroup_whenUpdatingGroupExist() {
        Group expected = new Group(101, "upd");
        groupDao.update(expected);

        Group actual = groupDao.getById(101).orElse(new Group(-1, ""));

        assertEquals(expected, actual);
    }

    /*@Test
    void update_shouldNotUpdateGroup_whenUpdatingGroupNotExist()*/

}