package com.foxminded.view.reader;

import java.util.Scanner;

public class ConsoleReader implements Reader{

    private final Scanner scanner;

    public ConsoleReader() {
        scanner = new Scanner(System.in);
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public int readInt() {
        int integer = scanner.nextInt();
        scanner.nextLine();
        return integer;
    }

    @Override
    public void close() {
        scanner.close();
    }

}
