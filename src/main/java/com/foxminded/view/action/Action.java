package com.foxminded.view.action;

public interface Action {
    void findAllGroupsWithLessOrEqualsStudentsCount();

    void findAllStudentsRelatedToCourse();

    void addNewStudent();

    void deleteStudentById();

    void addStudentToTheCourse();

    void removeStudentFromCourse();

    void noAction();
}
