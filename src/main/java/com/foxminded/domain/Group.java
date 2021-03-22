package com.foxminded.domain;

import com.foxminded.dao.ConnectionFactory;
import com.foxminded.dao.DaoException;
import com.foxminded.domain.loader.Loader;
import com.foxminded.domain.loader.StudentsLoaderFromGroup;

import java.util.List;
import java.util.Objects;

public class Group {
    private int id;
    private String name;
    private List<Student> students;

    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() throws DaoException {
        if (students == null) {
            Loader<Student> studentLoader = new StudentsLoaderFromGroup(new ConnectionFactory());
            students = studentLoader.load(id);
        }
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id && name.equals(group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
