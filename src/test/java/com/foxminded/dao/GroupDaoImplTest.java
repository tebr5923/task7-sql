package com.foxminded.dao;

import com.foxminded.domain.Group;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDaoImplTest {
    private static Connection connection;
    private static DBFactory dbFactory;

    private GroupDaoImpl groupDao;

    @org.junit.jupiter.api.BeforeAll
    public static void createTables() {
        //create Tables
        dbFactory = new DBFactory();
        System.out.println("try create table...");
        dbFactory.createTables();
        connection = new DaoFactory().getConnection();
        System.out.println("Connection to H2 open");
    }

    @org.junit.jupiter.api.AfterAll
    public static void dropTables() throws SQLException {
        //drop Tables
        dbFactory.dropTables();
        connection.close();
        System.out.println("Connection to H2 close");
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        groupDao = new GroupDaoImpl();
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

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        groupDao = new GroupDaoImpl();
        //String sql = "TRUNCATE TABLE groups;";
        //String sql = "DELETE FROM groups;DBCC CHECKIDENT ('groups', RESEED, 1);";
        String sql = "DELETE FROM groups;ALTER SEQUENCE groups_id_seq RESTART WITH 101";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("execute query for data save");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("WRONG QUERY");
        }
    }

    @org.junit.jupiter.api.Test
    void getByName_shouldReturnGroup_whenGetGroupWhichExist() {
        Group expected = new Group(102, "save2");

        Group actual = groupDao.getByName("save2");

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getByName_shouldReturnEmptyGroup_whenGetGroupWhichNotExist() {
        Group expected = new Group(-1, "");

        Group actual = groupDao.getByName("ntExt");

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getById_shouldReturnGroup_whenGetGroupWhichExist() {
        Group expected = new Group(102, "save2");

        Group actual = groupDao.getById(102);

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getById_shouldReturnEmptyGroup_whenGetGroupWhichNotExist() {
        Group expected = new Group(-1, "");

        Group actual = groupDao.getById(55);

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getAll_shouldReturnAllGroups() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(101, "save1"));
        expected.add(new Group(102, "save2"));

        List<Group> actual = groupDao.getAll();

        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void save_shouldSaveGroup_whenSavingGroupNotExist() {
        Group expected = new Group();
        expected.setName("new-1");
        groupDao.save(expected);

        Group actual = new Group();
        String query = "SELECT * FROM groups g WHERE g.name='new-1'";
        try (ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            try {
                if (resultSet.next()) {
                    actual.setId(resultSet.getInt("id"));
                    actual.setName(resultSet.getString("name"));
                }
            } catch (SQLException e) {
                System.err.println(" Wrong ResultSet!!! ");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println(" Wrong query!!! ");
            e.printStackTrace();
        }
        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void update_shouldUpdateGroup_whenUpdatingGroupExist() {
        Group expected = new Group(101, "upd");
        groupDao.update(expected);

        Group actual = new Group();
        String query = "SELECT * FROM groups g WHERE g.id=101";
        try (ResultSet resultSet = connection.createStatement().executeQuery(query)) {
            try {
                if (resultSet.next()) {
                    actual.setId(resultSet.getInt("id"));
                    actual.setName(resultSet.getString("name"));
                }
            } catch (SQLException e) {
                System.err.println(" Wrong ResultSet!!! ");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println(" Wrong query!!! ");
            e.printStackTrace();
        }
        assertEquals(expected, actual);
    }

    /*@org.junit.jupiter.api.Test
    void update_shouldNotUpdateGroup_whenUpdatingGroupNotExist()*/

}