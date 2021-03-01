package com.foxminded.dao;

import com.foxminded.domain.Group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("squid:S106")
public class GroupDaoImpl implements GroupDao {
    private static final String WRONG_QUERY = " Wrong query!!! ";
    private static final String WRONG_RESULT_SET = " Wrong ResultSet!!! ";

    private final ConnectionProvider connectionProvider;

    public GroupDaoImpl(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Group> getByName(String name) throws DaoException {
        String sql = "SELECT * FROM groups g WHERE g.name=?";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Group group = new Group();
                    group.setId(resultSet.getInt("id"));
                    group.setName(resultSet.getString("name"));
                    System.out.println("GET BY name OK... group with name " + name);
                    return Optional.of(group);
                }
            } catch (SQLException e) {
                System.err.println(WRONG_RESULT_SET);
                throw new DaoException(WRONG_RESULT_SET, e);
            }
        } catch (SQLException e) {
            System.err.println(WRONG_QUERY);
            throw new DaoException(WRONG_QUERY, e);
        }
        System.err.println("NOT FOUND!!!!... group with name " + name);
        return Optional.empty();
    }

    @Override
    public Optional<Group> getById(Integer id) throws DaoException {
        String sql = "SELECT * FROM groups g WHERE g.id=?";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Group group = new Group();
                    group.setId(resultSet.getInt("id"));
                    group.setName(resultSet.getString("name"));
                    System.out.println("GET BY id OK... group with id " + id);
                    return Optional.of(group);
                }
            } catch (SQLException e) {
                System.err.println(WRONG_RESULT_SET);
                throw new DaoException(WRONG_RESULT_SET, e);
            }
        } catch (SQLException e) {
            System.err.println(WRONG_QUERY);
            throw new DaoException(WRONG_QUERY, e);
        }
        System.err.println("NOT FOUND!!!!... group with id " + id);
        return Optional.empty();
    }

    @Override
    public List<Group> getAll() throws DaoException {
        List<Group> groupList = new ArrayList<>();
        String sql = "SELECT * FROM groups";
        try (final Connection connection = connectionProvider.getConnection();
             final ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                Group group = new Group();
                group.setId(resultSet.getInt("id"));
                group.setName(resultSet.getString("name"));
                groupList.add(group);
            }
        } catch (SQLException e) {
            System.err.println(WRONG_QUERY);
            throw new DaoException(WRONG_QUERY, e);
        }
        System.out.println("GET ALL group OK...");
        return groupList;
    }

    @Override
    public void save(Group model) throws DaoException {
        String sql = "INSERT INTO groups (id, name) values(DEFAULT,?)";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getName());
            statement.executeUpdate();
            System.out.println("SAVE OK..." + " group with name " + model.getName());
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getInt(1));
                }
            } catch (SQLException e) {
                System.err.println(WRONG_RESULT_SET);
                throw new DaoException(WRONG_RESULT_SET, e);
            }
        } catch (SQLException e) {
            System.err.println(WRONG_QUERY);
            throw new DaoException(WRONG_QUERY, e);
        }
    }

    @Override
    public void update(Group model) throws DaoException {
        String sql = "UPDATE groups set name=? WHERE id=?";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, model.getName());
            statement.setInt(2, model.getId());
            statement.executeUpdate();
            System.out.println("UPDATE OK... group " + model);
        } catch (SQLException e) {
            System.err.println(WRONG_QUERY);
            throw new DaoException(WRONG_QUERY, e);
        }
    }

    @Override
    public void delete(Group model) throws DaoException {
        String sql = "DELETE FROM groups WHERE id=?";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, model.getId());
            if (statement.executeUpdate() == 0) {
                System.err.println("FAIL DELETE group " + model);
                throw new DaoException("group not exist - delete FAIL");
            }
            System.out.println("DELETE OK... group " + model);
        } catch (SQLException e) {
            System.err.println(WRONG_QUERY);
            throw new DaoException(WRONG_QUERY, e);
        }
    }
}
