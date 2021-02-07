package com.foxminded.dao;

import com.foxminded.reader.ResourceFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.stream.Collectors;

public class DaoFactory {
    private static final String PROP_FILE_NAME = "db.properties";
    private static final String SCHEMA_FILE_NAME = "schema.sql";
    private static final DaoFactory INSTANCE = new DaoFactory();

    private final Properties properties = new Properties();

    private DaoFactory() {
        try (InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(PROP_FILE_NAME)) {
            if (inputStream == null) {
                throw new FileNotFoundException("file " + PROP_FILE_NAME + " not found");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        createDB();
    }

    public static DaoFactory getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return connection;
    }

    private void createDB() {
        ResourceFileReader resourceFileReader = new ResourceFileReader();
        String script = "";
        try {
            script = resourceFileReader.read(SCHEMA_FILE_NAME).collect(Collectors.joining());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        try (final Connection connection = getConnection();
             final PreparedStatement statement = connection.prepareStatement(script)) {
            statement.execute();
            System.out.println("table create");
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
