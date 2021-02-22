package com.foxminded.dao;

import com.foxminded.domain.Group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDaoImpl implements GroupDao {
    private final DaoFactory daoFactory;

    public GroupDaoImpl() {
        this.daoFactory = new DaoFactory();
    }

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
                    System.out.println("GET BY name OK... group with name " + name);
                    return group;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.err.println("NOT FOUND!!!!... group with name " + name);
        return new Group(-1, "");
    }

    @Override
    public Group getById(Integer id) {
        String sql = "SELECT * FROM groups g WHERE g.id=?";
        try (final Connection connection = daoFactory.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Group group = new Group();
                    group.setId(resultSet.getInt("id"));
                    group.setName(resultSet.getString("name"));
                    System.out.println("GET BY id OK... group with id " + id);
                    return group;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.err.println("NOT FOUND!!!!... group with id " + id);
        return new Group(-1, "");
    }

    @Override
    public List<Group> getAll() {
        List<Group> groupList = new ArrayList<>();
        String sql = "SELECT * FROM groups";
        try (final Connection connection = daoFactory.getConnection();
             final ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                groupList.add(new Group(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("GET ALL group OK...");
        return groupList;
    }

    @Override
    public void save(Group model) {
        String sql = "INSERT INTO groups (id, name) values(DEFAULT,?)";
        try (final Connection connection = daoFactory.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getName());
            statement.executeUpdate();
            System.out.println("SAVE OK..." + " group with name " + model.getName());
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Group model) {
        String sql = "UPDATE groups set name=? WHERE id=?";
        try (final Connection connection = daoFactory.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, model.getName());
            statement.setInt(2, model.getId());
            statement.executeUpdate();
            System.out.println("UPDATE OK... group " + model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Group model) throws DaoException {
        String sql = "DELETE FROM groups WHERE id=?";
        try (final Connection connection = daoFactory.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, model.getId());
            if (statement.executeUpdate() == 0) {
                System.err.println("FAIL DELETE group " + model);
                throw new DaoException("group not exist - delete FAIL");
            }
            System.out.println("DELETE OK... group " + model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
