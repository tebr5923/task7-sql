package com.foxminded.generator;

import com.foxminded.domain.Course;
import com.foxminded.reader.Reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class CourseGenerator implements Generator<Course> {
    private static final String DATA_FILE_NAME = "data/courses.data";

    private final Reader reader;

    public CourseGenerator(Reader reader) {
        this.reader = reader;
    }

    @Override
    public List<Course> generate(int size) {
        try {
            return reader.read(DATA_FILE_NAME)
                    .map(this::generateCourse)
                    .limit(size)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private Course generateCourse(String name) {
        Course course = new Course();
        course.setName(name);
        course.setDescription(String.format("this is %s", name));
        return course;
    }
}
