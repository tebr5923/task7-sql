package com.foxminded.dao;

import com.foxminded.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection() {
        final Config config = Config.getInstance();
        Connection connection;
        try {
            connection = DriverManager.getConnection(config.value("db.url"), config.value("db.user"), config.value("db.password"));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return connection;
    }
}
