package com.foxminded.dao;

import com.foxminded.reader.ResourceFileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DBFactory {
    private static final String SCHEMA_FILE_NAME = "schema.sql";
    private static final String DROP_TABLES_FILE_NAME = "drop.sql";

    public void createTables() {
        ResourceFileReader resourceFileReader = new ResourceFileReader();
        String script = "";
        try {
            script = resourceFileReader.read(SCHEMA_FILE_NAME).collect(Collectors.joining());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        DaoFactory daoFactory = new DaoFactory();
        try (final Connection connection = daoFactory.getConnection();
             final Statement statement = connection.createStatement()) {
            statement.execute(script);
            System.out.println("table create");
        } catch (SQLException e) {
            System.err.println("table NOT create");
            throw new IllegalStateException("cant create table", e);
        }
    }

    public void dropTables() {
        ResourceFileReader resourceFileReader = new ResourceFileReader();
        String script = "";
        try {
            script = resourceFileReader.read(DROP_TABLES_FILE_NAME).collect(Collectors.joining());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        DaoFactory daoFactory = new DaoFactory();
        try (final Connection connection = daoFactory.getConnection();
             final Statement statement = connection.createStatement()) {
            statement.execute(script);
            System.out.println("DROP TABLES");
        } catch (SQLException e) {
            System.err.println("tables NOT DROP");
            throw new IllegalStateException("cant DROP tables", e);
        }
    }
}
