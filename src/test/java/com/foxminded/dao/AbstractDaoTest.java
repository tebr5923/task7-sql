package com.foxminded.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.SQLException;

abstract class AbstractDaoTest {
    protected static Connection connection;
    protected static DBFactory dbFactory;

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
}
