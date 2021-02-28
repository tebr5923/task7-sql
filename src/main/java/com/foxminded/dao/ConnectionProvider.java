package com.foxminded.dao;

import java.sql.Connection;

public interface ConnectionProvider {
    Connection getConnection();
}
