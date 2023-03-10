package com.foxminded.view;

import com.foxminded.dao.ConnectionProviderDaoFactory;
import com.foxminded.dao.PropertyConnectionProvider;
import com.foxminded.view.reader.ConsoleReader;
import com.foxminded.view.action.Action;
import com.foxminded.view.action.ConsoleAction;
import com.foxminded.view.reader.Reader;

@SuppressWarnings("squid:S106")//Console application
public class ConsoleMenu {
    private static final Reader CONSOLE_READER = new ConsoleReader();

    private final Reader consoleReader;
    private final Action action;

    public ConsoleMenu() {
        this(CONSOLE_READER);
    }

    public ConsoleMenu(Reader consoleReader) {
        this.consoleReader = consoleReader;
        this.action = new ConsoleAction(consoleReader, new ConnectionProviderDaoFactory(new PropertyConnectionProvider()));
    }

    public void showMainMenu() {
        String mainString = "Chose an action and press the button and press ENTER";
        String menuItemA = "a. Find all groups with less or equals students count";
        String menuItemB = "b. Find all students related to course with given name";
        String menuItemC = "c. Add new student";
        String menuItemD = "d. Delete student by STUDENT_ID";
        String menuItemE = "e. Add a student to the course (from a list)";
        String menuItemF = "f. Remove the student from one of his or her courses";
        String menuExit = "q. exit";

        System.out.println(mainString);
        System.out.println(menuItemA);
        System.out.println(menuItemB);
        System.out.println(menuItemC);
        System.out.println(menuItemD);
        System.out.println(menuItemE);
        System.out.println(menuItemF);
        System.out.println(menuExit);
    }

    public void readFromConsole() {
        // i don't sure about needs this try-with-resources in this place
        // this is for experience with self-made AutoCloseable
        try (Reader reader = consoleReader) {
            String s = reader.readString();
            while (!s.equals("q")) {
                switch (s) {
                    case ("a"):
                        action.findAllGroupsWithLessOrEqualsStudentsCount();
                        break;
                    case ("b"):
                        action.findAllStudentsRelatedToCourse();
                        break;
                    case ("c"):
                        action.addNewStudent();
                        break;
                    case ("d"):
                        action.deleteStudentById();
                        break;
                    case ("e"):
                        action.addStudentToTheCourse();
                        break;
                    case ("f"):
                        action.removeStudentFromCourse();
                        break;
                    default:
                        action.noAction();
                        break;
                }
                showMainMenu();
                s = reader.readString();
            }
        } catch (Exception e) {
            System.err.println("Can't close reader, maybe it's already close!");
            e.printStackTrace();
            throw new IllegalStateException("Can't close reader, maybe it's already close!", e);
        }
    }
}
