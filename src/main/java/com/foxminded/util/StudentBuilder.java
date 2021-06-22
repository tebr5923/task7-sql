package com.foxminded.util;

import com.foxminded.domain.Student;

public class StudentBuilder {
    public Student build(int id, int groupId, String firstName, String lastName) {
        Student student = new Student();
        student.setId(id);
        student.setGroupId(groupId);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        return student;
    }

    public Student build(int id, String firstName, String lastName) {
        Student student = new Student();
        student.setId(id);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        return student;
    }

    public Student build(String firstName, String lastName) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        return student;
    }
}
