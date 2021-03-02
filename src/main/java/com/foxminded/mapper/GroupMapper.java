package com.foxminded.mapper;

import com.foxminded.domain.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements Mapper<Group> {
    @Override
    public Group map(ResultSet resultSet) throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getInt("id"));
        group.setName(resultSet.getString("name"));
        return group;
    }

    @Override
    public void map(PreparedStatement statement, Group model) throws SQLException {
        statement.setString(1, model.getName());
        statement.setInt(2, model.getId());
    }
}
