package com.foxminded.generator;

import com.foxminded.domain.Student;
import com.foxminded.reader.ResourceFileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentGenerator implements Generator<Student> {
    private static final String FIRSTNAME_FILE_NAME = "data/firstname.data";
    private static final String LASTNAME_FILE_NAME = "data/lastname.data";

    @Override
    public List<Student> generate(int size) throws IOException, URISyntaxException {
        return generateStudentList(getFirstnames(), getLastnames(), size);
    }

    private List<String> getFirstnames() throws IOException, URISyntaxException {
        return new ResourceFileReader().read(FIRSTNAME_FILE_NAME)
                .collect(Collectors.toList());
    }

    private List<String> getLastnames() throws IOException, URISyntaxException {
        return new ResourceFileReader().read(LASTNAME_FILE_NAME)
                .collect(Collectors.toList());
    }

    private List<Student> generateStudentList(List<String> firstnameList, List<String> lastnameList, int bound) {
        return Stream.generate(()->buildStudent(firstnameList.get(generateDigit(firstnameList.size())), lastnameList.get(generateDigit(lastnameList.size()))))
                .limit(bound)
                .collect(Collectors.toList());
    }

    private Student buildStudent(String firstname, String lastname){
        Student student = new Student();
        student.setFirstName(firstname);
        student.setLastName(lastname);
        return student;
    }

    private int generateDigit(int bound) {
        return new Random().nextInt(bound);
    }
}
