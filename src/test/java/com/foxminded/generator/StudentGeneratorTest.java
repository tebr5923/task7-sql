package com.foxminded.generator;

import com.foxminded.domain.Student;
import com.foxminded.reader.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentGeneratorTest {
    private static final String FIRSTNAME_FILE_NAME = "data/firstname.data";
    private static final String LASTNAME_FILE_NAME = "data/lastname.data";

    @Mock
    private Reader mockReader;
    @Mock
    private Random mockRandom;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        Stream<String> firstnameStream = Stream.of("Ivan", "Petr", "Roman");
        Stream<String> lastnameStream = Stream.of("Ivanov", "Petrov", "Romanov");
        when(mockReader.read(Mockito.eq(FIRSTNAME_FILE_NAME))).thenReturn(firstnameStream);
        when(mockReader.read(Mockito.eq(LASTNAME_FILE_NAME))).thenReturn(lastnameStream);

        when(mockRandom.nextInt(Mockito.anyInt())).thenReturn(1);
    }

    @Test
    void generate_shouldReturnCourseList() {
        Student petrov = new Student();
        petrov.setFirstName("Petr");
        petrov.setLastName("Petrov");
        List<Student> expected = Arrays.asList(petrov, petrov, petrov);

        List<Student> actual = new StudentGenerator(mockReader, mockRandom).generate(3);

        assertEquals(expected, actual);
    }
}
