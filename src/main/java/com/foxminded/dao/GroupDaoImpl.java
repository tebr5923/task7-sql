package com.foxminded.dao;

import com.foxminded.domain.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupDaoImpl implements GroupDao {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    public Group getByName(String name) {
        String sql = "SELECT * FROM groups g WHERE g.name=?";

        try (final Connection connection = daoFactory.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Group group = new Group();
                    group.setId(resultSet.getInt("id"));
                    group.setName(resultSet.getString("name"));
                    return group;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Group(-1, "");
    }

    @Override
    public Group getById(Integer id) {
        String sql = "SELECT * FROM groups g WHERE g.id=?";

        try (final Connection connection = daoFactory.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Group group = new Group();
                    group.setId(resultSet.getInt("id"));
                    group.setName(resultSet.getString("name"));
                    return group;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Group(-1, "");
    }

    @Override
    public List<Group> getAll() {
        return null;
    }

    @Override
    public void save(Group model) {
        String sql = "INSERT INTO groups (id, name) values(DEFAULT,?)";

        try (final Connection connection = daoFactory.getConnection();
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
