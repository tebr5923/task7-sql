package com.foxminded.generator;

import com.foxminded.domain.Student;
import com.foxminded.reader.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
    private static final long seed = 10;

    @Mock
    private Reader mockReader;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        Stream<String> firstnameStream = Stream.of("Ivan", "Petr", "Roman");
        Stream<String> lastnameStream = Stream.of("Ivanov", "Petrov", "Romanov");
        when(mockReader.read(FIRSTNAME_FILE_NAME)).thenReturn(firstnameStream);
        when(mockReader.read(LASTNAME_FILE_NAME)).thenReturn(lastnameStream);
    }

    @Test
    void generate_shouldReturnCourseList() {
        Student petrovPetr = new Student();
        petrovPetr.setFirstName("Petr");
        petrovPetr.setLastName("Petrov");
        Student ivanovIvan = new Student();
        ivanovIvan.setFirstName("Ivan");
        ivanovIvan.setLastName("Ivanov");
        List<Student> expected = Arrays.asList(ivanovIvan, ivanovIvan, petrovPetr);

        List<Student> actual = new StudentGenerator(mockReader, new Random(seed)).generate(3);

        assertEquals(expected, actual);
    }
}
