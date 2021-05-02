package com.foxminded.assigner;

import com.foxminded.domain.Group;
import com.foxminded.domain.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AssignerStudentsToGroups {
    private static final int DEFAULT_MIN_STUDENTS_IN_GROUP = 10;
    private static final int DEFAULT_MAX_STUDENTS_IN_GROUP = 30;

    private final int minStudentsInGroup;
    private final int maxStudentsInGroup;

    public AssignerStudentsToGroups() {
        this(DEFAULT_MIN_STUDENTS_IN_GROUP, DEFAULT_MAX_STUDENTS_IN_GROUP);
    }

    public AssignerStudentsToGroups(int minStudentsInGroup, int maxStudentsInGroup) {
        this.minStudentsInGroup = minStudentsInGroup;
        this.maxStudentsInGroup = maxStudentsInGroup;
    }

    public List<Group> assign(List<Student> studentList, List<Group> groupList) {
        List<Student> tempStudentList = new ArrayList<>(studentList);
        for (Group group : groupList) {
            group.setStudents(
                    generateAssignedList(tempStudentList)
            );
        }
        return groupList;
    }

    private List<Student> generateAssignedList(List<Student> studentList) {
        List<Student> assignedList = new ArrayList<>();
        if (studentList.size() < minStudentsInGroup) {
            return assignedList;
        }
        int bound = maxStudentsInGroup - minStudentsInGroup;
        int size = new Random().nextInt(bound) + minStudentsInGroup;
        int count = 0;
        while (count <= size && !studentList.isEmpty()) {
            count++;
            int index = new Random().nextInt(studentList.size());
            assignedList.add(studentList.remove(index));
        }
        return assignedList;
    }
}
