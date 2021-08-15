package com.foxminded;

import com.foxminded.assigner.Assigner;
import com.foxminded.assigner.CourseAssigner;
import com.foxminded.assigner.GroupAssigner;
import com.foxminded.dao.*;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.generator.CourseGenerator;
import com.foxminded.generator.GroupGenerator;
import com.foxminded.generator.StudentGenerator;
import com.foxminded.reader.ResourceFileReader;
import com.foxminded.view.Console;

import java.util.List;

@SuppressWarnings({"squid:S125", "squid:S1192", "squid:S106"})
public class Main {
    public static void main(String[] args) throws DaoException {
        ConnectionProvider provider = new PropertyConnectionProvider();
        DBFactory dbFactory = new DBFactory(provider);
        //dbFactory.dropTables();
        dbFactory.createTables();

        StudentDao studentDao = new StudentDaoImpl(provider);
        GroupDao groupDao = new GroupDaoImpl(provider, studentDao);
        CourseDao courseDao = new CourseDaoImpl(provider);

        //GroupGenerator and save
        List<Group> groupList = new GroupGenerator().generate(10);
        groupDao.saveAll(groupList);

        //CourseGenerator and save
        List<Course> courseList = new CourseGenerator(new ResourceFileReader()).generate(10);
        courseDao.saveAll(courseList);

        //StudentGenerator and save
        List<Student> studentList = new StudentGenerator(new ResourceFileReader()).generate(200);

        GroupAssigner groupAssigner = new GroupAssigner();
        List<Group> assignedGroupList = groupAssigner.assign(groupList, studentList);

        Assigner<Student, Course> courseAssigner = new CourseAssigner();
        List<Student> assignedStudentList = courseAssigner.assign(studentList, courseList);
        studentDao.saveAll(assignedStudentList);

        Console console = new Console();
        console.showMainMenu();
        console.readFromConsole();

        //dbFactory.dropTables();
    }
}