package com.foxminded.generator;

import com.foxminded.domain.Student;
import com.foxminded.reader.Reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentGenerator implements Generator<Student> {
    private static final String FIRSTNAME_FILE_NAME = "data/firstname.data";
    private static final String LASTNAME_FILE_NAME = "data/lastname.data";

    private final Reader reader;
    private final Random random;

    public StudentGenerator(Reader reader) {
        this(reader, new Random());
    }

    public StudentGenerator(Reader reader, Random random) {
        this.reader = reader;
        this.random = random;
    }

    @Override
    public List<Student> generate(int size) {
        return generateStudentList(getNames(FIRSTNAME_FILE_NAME), getNames(LASTNAME_FILE_NAME), size);
    }

    private List<String> getNames(String filename) {
        try {
            return reader.read(filename).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private List<Student> generateStudentList(List<String> firstnameList, List<String> lastnameList, int bound) {
        return Stream.generate(() -> buildStudent(firstnameList.get(generateDigit(firstnameList.size())), lastnameList.get(generateDigit(lastnameList.size()))))
                .limit(bound)
                .collect(Collectors.toList());
    }

    private Student buildStudent(String firstname, String lastname) {
        Student student = new Student();
        student.setFirstName(firstname);
        student.setLastName(lastname);
        return student;
    }

    private int generateDigit(int bound) {
        return random.nextInt(bound);
    }
}
