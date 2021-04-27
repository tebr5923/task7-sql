package com.foxminded.generator;

import com.foxminded.domain.Course;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseGeneratorTest {
    @Mock
    private Reader mockReader;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        Stream<String> stringStream = Stream.of("History", "Math", "Economics", "Biology");
        when(mockReader.read(Mockito.anyString())).thenReturn(stringStream);
    }

    @Test
    void generate_shouldReturnCourseList() {
        Course history = new Course();
        history.setName("History");
        history.setDescription("this is History");
        Course math = new Course();
        math.setName("Math");
        math.setDescription("this is Math");
        Course economics = new Course();
        economics.setName("Economics");
        economics.setDescription("this is Economics");
        List<Course> expected = Arrays.asList(history, math, economics);

        List<Course> actual = new CourseGenerator(mockReader).generate(3);

        assertEquals(expected, actual);
    }
}
