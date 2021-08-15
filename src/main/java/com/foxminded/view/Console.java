package com.foxminded.view;

import java.util.Scanner;

@SuppressWarnings("squid:S106")//Console application
public class Console {
    public void mainMenu() {
        String mainString = "Chose an action and press the button and press ENTER";
        String menuItemA = "a. Find all groups with less or equals student count";
        String menuItemB = "b. Find all students related to course with given name";
        String menuItemC = "c. Add new student";
        String menuItemD = "d. Delete student by STUDENT_ID";
        String menuItemE = "e. Add a student to the course (from a list)";
        String menuItemF = "f. Remove the student from one of his or her courses";

        System.out.println(mainString);
        System.out.println(menuItemA);
        System.out.println(menuItemB);
        System.out.println(menuItemC);
        System.out.println(menuItemD);
        System.out.println(menuItemE);
        System.out.println(menuItemF);
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        do {
            System.out.println("вы ввели " + s);
            s = scanner.nextLine();
        } while (!s.equals("q"));
        scanner.close();
    }
}
