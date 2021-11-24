package com.foxminded.assigner;

import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GroupAssignerTest {
    private static final long seed = 8;

    private Student ivanov;
    private Student petrov;
    private Student romanov;
    private Student denisov;
    private Student alexandrov;
    private Student alexeev;
    private Student pahomov;
    private Student mihailov;
    private Student egorov;
    private Student igorev;
    private List<Student> studentList;
    private List<Group> groupList;


    @BeforeEach
    void setUp() {
        ivanov = createStudent("Ivan", "Ivanov");
        petrov = createStudent("Petr", "Petrov");
        romanov = createStudent("Roman", "Romanov");
        denisov = createStudent("Denis", "Denisov");
        alexandrov = createStudent("Alexandr", "Alexandrov");
        alexeev = createStudent("Alexey", "Alexaeev");
        pahomov = createStudent("Pahom", "Pahomov");
        mihailov = createStudent("Mihail", "Mihailov");
        egorov = createStudent("Egor", "Egorov");
        igorev = createStudent("Igor", "Igorev");
        studentList = Arrays.asList(ivanov, petrov, romanov,
                denisov, alexandrov, alexeev, pahomov, mihailov, egorov, igorev);

        Group groupPG30 = new Group();
        groupPG30.setName("PG-30");
        Group groupES78 = new Group();
        groupES78.setName("ES-78");
        Group groupVC39 = new Group();
        groupVC39.setName("VC-39");
        groupList = Arrays.asList(groupPG30, groupES78, groupVC39);
    }

    @Test
    void assign_shouldReturnGroupListWithStudentList() {
        Group assignedGroupPG30 = new Group();
        assignedGroupPG30.setName("PG-30");
        assignedGroupPG30.setStudents(Arrays.asList(pahomov, petrov, ivanov));
        Group assignedGroupES78 = new Group();
        assignedGroupES78.setName("ES-78");
        assignedGroupES78.setStudents(Collections.singletonList(mihailov));
        Group assignedGroupVC39 = new Group();
        assignedGroupVC39.setName("VC-39");
        assignedGroupVC39.setStudents(Arrays.asList(igorev, alexeev, romanov, denisov));
        List<Group> expected = Arrays.asList(assignedGroupPG30, assignedGroupES78, assignedGroupVC39);

        Assigner<Group, Student> assigner = new GroupAssigner(1, 4, new Random(seed));
        List<Group> actual = assigner.assign(groupList, studentList);

        assertSize(actual);
        assertEquals(expected, actual);
        assertStudentList(expected, actual);
    }

    private void assertSize(List<Group> groupList) {
        int size = 0;
        for (Group group : groupList) {
            if (group.getStudents() != null) {
                assertTrue(group.getStudents().size() <= 4);
                size += group.getStudents().size();
            }
        }
        assertTrue(size <= studentList.size());
    }

    private void assertStudentList(List<Group> expected, List<Group> actual) {
        ListIterator<Group> groupIterator = actual.listIterator();
        while (groupIterator.hasNext()) {
            assertEquals(expected.get(groupIterator.nextIndex()).getStudents(), groupIterator.next().getStudents());
        }
    }

    private Student createStudent(String firstName, String lastName) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        return student;
    }
}
