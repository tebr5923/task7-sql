package com.foxminded.console_reader;

import java.util.Scanner;

public class ConsoleReader {

    private final Scanner scanner;

    public ConsoleReader() {
        scanner = new Scanner(System.in);
    }

    public String readString() {
        return scanner.nextLine();
    }

    public int readInt() {
        int integer = scanner.nextInt();
        scanner.nextLine();
        return integer;
    }

    public void close() {
        scanner.close();
    }

}
