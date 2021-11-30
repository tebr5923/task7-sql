package com.foxminded.dao;

public class ConnectionProviderDaoFactory implements DaoFactory {
    private final ConnectionProvider connectionProvider;

    public ConnectionProviderDaoFactory(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public StudentDao createStudentDao() {
        return new StudentDaoImpl(connectionProvider);
    }

    @Override
    public GroupDao createGroupDao(StudentDao studentDao) {
        return new GroupDaoImpl(connectionProvider, studentDao);
    }

    @Override
    public CourseDao createCourseDao() {
        return new CourseDaoImpl(connectionProvider);
    }
}
