package com.foxminded.mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {
    T map(ResultSet resultSet) throws SQLException;

    void map(PreparedStatement statement, T model) throws SQLException;
}
