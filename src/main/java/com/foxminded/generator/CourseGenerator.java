package com.foxminded.generator;

import com.foxminded.domain.Course;
import com.foxminded.reader.ResourceFileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class CourseGenerator implements Generator<Course> {
    private static final String DATA_FILE_NAME = "data/courses.data";

    @Override
    public List<Course> generate(int size) throws IOException, URISyntaxException {
        return new ResourceFileReader().read(DATA_FILE_NAME)
                .map(this::generateCourse)
                .limit(size)
                .collect(Collectors.toList());
    }

    private Course generateCourse(String name) {
        Course course = new Course();
        course.setName(name);
        course.setDescription(String.format("this is %s", name));
        return course;
    }
}
