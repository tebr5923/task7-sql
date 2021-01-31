package com.foxminded.domain;

import java.util.Objects;

public class Students {
    private int id;
    private int groupId;
    private String firstName;
    private String lastName;

    public Students() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Students students = (Students) o;
        return id == students.id && groupId == students.groupId && Objects.equals(firstName, students.firstName) && Objects.equals(lastName, students.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Students{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
