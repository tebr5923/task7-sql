package com.foxminded.domain;

import java.util.Objects;

public class StudentsCourses {
    private int studentId;
    private int courseId;

    public StudentsCourses() {
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentsCourses that = (StudentsCourses) o;
        return studentId == that.studentId && courseId == that.courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }

    @Override
    public String toString() {
        return "StudentsCourses{" +
                "studentId=" + studentId +
                ", courseId=" + courseId +
                '}';
    }
}
