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
import com.foxminded.view.ConsoleMenu;

import java.util.List;
import java.util.Random;

@SuppressWarnings({"squid:S125", "squid:S1192", "squid:S106"})
public class Main {
    public static void main(String[] args) throws DaoException {
        Random random = new Random(10);

        ConnectionProvider provider = new PropertyConnectionProvider();
        DBFactory dbFactory = new DBFactory(provider);
        dbFactory.dropTables();
        dbFactory.createTables();

        StudentDao studentDao = new StudentDaoImpl(provider);
        GroupDao groupDao = new GroupDaoImpl(provider, studentDao);
        CourseDao courseDao = new CourseDaoImpl(provider);

        //GroupGenerator and save
        List<Group> groupList = new GroupGenerator(random).generate(10);
        groupDao.saveAll(groupList);

        //CourseGenerator and save
        List<Course> courseList = new CourseGenerator(new ResourceFileReader()).generate(10);
        courseDao.saveAll(courseList);

        //StudentGenerator and save
        List<Student> studentList = new StudentGenerator(new ResourceFileReader(), random).generate(200);

        GroupAssigner groupAssigner = new GroupAssigner(random);
        groupAssigner.assign(groupList, studentList);

        Assigner<Student, Course> courseAssigner = new CourseAssigner(random);
        List<Student> assignedStudentList = courseAssigner.assign(studentList, courseList);
        studentDao.saveAll(assignedStudentList);

        ConsoleMenu consoleMenu = new ConsoleMenu();
        consoleMenu.showMainMenu();
        consoleMenu.readFromConsole();

        //dbFactory.dropTables();
    }
}
