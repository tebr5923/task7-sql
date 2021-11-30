package com.foxminded.dao;

public interface DaoFactory {
    StudentDao createStudentDao();

    GroupDao createGroupDao(StudentDao studentDao);

    CourseDao createCourseDao();
}
