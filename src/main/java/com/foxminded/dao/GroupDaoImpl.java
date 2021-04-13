package com.foxminded.dao;

import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.mapper.GroupMapper;
import com.foxminded.mapper.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("squid:S106") //dont use logger in this task
public class GroupDaoImpl implements GroupDao {
    private static final Mapper<Group> GROUP_MAPPER = new GroupMapper();
    private static final StudentDao studentDao = new StudentDaoImpl(new ConnectionFactory()); //TODO: must be not static
    private final ConnectionProvider connectionProvider;

    public GroupDaoImpl(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Group> getByName(String name) throws DaoException {
        String sql = "select g.id as group_id, g.name from groups g where g.name=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Group group = new DbGroup(GROUP_MAPPER.map(resultSet));
                    System.out.println("GET BY name OK... group with name " + name);
                    return Optional.of(group);
                }
            }
        } catch (SQLException e) {
            System.err.println("cant get group by name!!!");
            throw new DaoException("cant get group by name!!!", e);
        }
        System.err.println("NOT FOUND!!!!... group with name " + name);
        return Optional.empty();
    }

    @Override
    public Optional<Group> getById(Integer id) throws DaoException {
        String sql = "select g.id as group_id, g.name from groups g where g.id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Group group = new DbGroup(GROUP_MAPPER.map(resultSet));
                    //Group group = GROUP_MAPPER.map(resultSet);
                    System.out.println("GET BY id OK... group with id " + id);
                    return Optional.of(group);
                }
            }
        } catch (SQLException e) {
            System.err.println("cant get group by id!!!");
            throw new DaoException("cant get group by id!!!", e);
        }
        System.err.println("NOT FOUND!!!!... group with id " + id);
        return Optional.empty();
    }

    @Override
    public List<Group> getAll() throws DaoException {
        List<Group> groupList = new ArrayList<>();
        String sql = "select g.id as group_id, g.name from groups g;";
        try (final Connection connection = connectionProvider.getConnection();
             final ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                Group group = new DbGroup(GROUP_MAPPER.map(resultSet));
                //Group group = GROUP_MAPPER.map(resultSet);
                groupList.add(group);
            }
        } catch (SQLException e) {
            System.err.println("cant get all groups");
            throw new DaoException("cant get all groups", e);
        }
        System.out.println("GET ALL group OK...");
        return groupList;
    }

    @Override
    public void save(Group model) throws DaoException {
        String sql = "INSERT INTO groups (id, name) values(DEFAULT,?);";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            GROUP_MAPPER.map(statement, model);
            statement.executeUpdate();
            System.out.println("SAVE OK..." + " group with name " + model.getName());
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    model.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("cant save group");
            throw new DaoException("cant save group", e);
        }
    }

    @Override
    public void update(Group model) throws DaoException {
        String sql = "UPDATE groups set name=? WHERE id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            GROUP_MAPPER.map(statement, model);
            statement.executeUpdate();
            System.out.println("UPDATE OK... group " + model);
        } catch (SQLException e) {
            System.err.println("cant update group");
            throw new DaoException("cant update group", e);
        }
    }

    @Override
    public void delete(Group model) throws DaoException {
        String sql = "DELETE FROM groups WHERE id=?;";
        try (final Connection connection = connectionProvider.getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, model.getId());
            if (statement.executeUpdate() == 0) {
                System.err.println("FAIL DELETE group " + model);
                throw new DaoException("group not exist - delete FAIL");
            }
            System.out.println("DELETE OK... group " + model);
        } catch (SQLException e) {
            System.err.println("cant delete group");
            throw new DaoException("cant delete group", e);
        }
    }

    private class DbGroup extends Group {
        private DbGroup(Group group) {
            setId(group.getId());
            setName(group.getName());
        }

        @Override
        public List<Student> getStudents() throws DaoException {
            return studentDao.getStudentsByGroup(getId());
        }

    }
}
