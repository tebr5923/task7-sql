package com.foxminded.assigner;

import com.foxminded.domain.Group;
import com.foxminded.domain.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupAssigner implements Assigner<Group, Student> {
    private static final int DEFAULT_MIN_STUDENTS_IN_GROUP = 10;
    private static final int DEFAULT_MAX_STUDENTS_IN_GROUP = 30;

    private final int minStudentsInGroup;
    private final int maxStudentsInGroup;
    private final Random random;

    public GroupAssigner() {
        this(DEFAULT_MIN_STUDENTS_IN_GROUP, DEFAULT_MAX_STUDENTS_IN_GROUP, new Random());
    }

    public GroupAssigner(Random random) {
        this(DEFAULT_MIN_STUDENTS_IN_GROUP, DEFAULT_MAX_STUDENTS_IN_GROUP, random);
    }

    public GroupAssigner(int minStudentsInGroup, int maxStudentsInGroup) {
        this(minStudentsInGroup, maxStudentsInGroup, new Random());
    }

    public GroupAssigner(int minStudentsInGroup, int maxStudentsInGroup, Random random) {
        this.minStudentsInGroup = minStudentsInGroup;
        this.maxStudentsInGroup = maxStudentsInGroup;
        this.random = random;
    }

    @Override
    public List<Group> assign(List<Group> groupList, List<Student> studentList) {
        List<Student> tempStudentList = new ArrayList<>(studentList);
        groupList.forEach(g -> g.setStudents(generateAssignedList(g.getId(), tempStudentList)));
        return groupList;
    }

    private Student setGroupIdToStudent(int groupId, Student student) {
        student.setGroupId(groupId);
        return student;
    }

    private List<Student> generateAssignedList(int groupId, List<Student> studentList) {
        List<Student> assignedList = new ArrayList<>();
        if (studentList.size() < minStudentsInGroup) {
            return assignedList;
        }
        int bound = maxStudentsInGroup - minStudentsInGroup;
        int size = random.nextInt(bound + 1) + minStudentsInGroup;
        int count = 0;
        while (count < size && !studentList.isEmpty()) {
            count++;
            int index = random.nextInt(studentList.size());
            //assignedList.add(studentList.remove(index));
            assignedList.add(setGroupIdToStudent(groupId, studentList.remove(index)));
        }
        return assignedList;
    }
}
