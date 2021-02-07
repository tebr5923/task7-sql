package com.foxminded.dao;

import com.foxminded.domain.Group;
import com.foxminded.domain.Student;

import java.sql.*;
import java.util.List;

public class GroupDatabaseDao implements GroupDao {
    private final ConnectionFactory connectionFactory;

    public GroupDatabaseDao() {
        this.connectionFactory = new ConnectionFactory();
    }

    @Override
    public Group getByName(String name) {
        String sql = "SELECT * FROM groups g WHERE g.name=?";
        Group group = null;

        try (final Connection connection = connectionFactory.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    group = new Group();
                    group.setId(resultSet.getInt("id"));
                    group.setName(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    @Override
    public List<Student> getStudents() {
        return null;
    }

    @Override
    public Group getById(Integer id) {
        String sql = "SELECT * FROM groups g WHERE g.id=?";
        Group group = null;

        try (final Connection connection = connectionFactory.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    group = new Group();
                    group.setId(resultSet.getInt("id"));
                    group.setName(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    @Override
    public List<Group> getAll() {
        return null;
    }

    @Override
    public void save(Group model) {
        String sql = "INSERT INTO groups (id, name) values(DEFAULT,?)";

        try (final Connection connection = connectionFactory.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, model.getName());
            statement.execute();
            System.out.println("save ok");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Group model) {

    }

    @Override
    public void delete(Group model) {

    }
}
