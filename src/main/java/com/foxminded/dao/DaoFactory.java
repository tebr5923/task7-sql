package com.foxminded.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DaoFactory {
    private static final String PROP_FILE_NAME = "db.properties";

    private final Properties properties;

    public DaoFactory() {
        this.properties = new Properties();
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
    }


    public Connection getConnection() {
        Connection connection;

        try {
            Class.forName(properties.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );
            System.out.println("Connection OK");
        } catch (SQLException e) {
            System.err.println("Connection Error");
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        return connection;
    }
}
