package com.foxminded.generator;

import com.foxminded.domain.Group;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupGenerator implements Generator<Group> {
    @Override
    public List<Group> generate(int size) {
        return Stream.generate(this::generateGroup)
                .limit(size)
                .collect(Collectors.toList());
    }

    private Group generateGroup() {
        Group group = new Group();
        String name = String.valueOf(generateChar()) + generateChar() + '-' + generateDigit() + generateDigit();
        group.setName(name);
        return group;
    }

    private char generateChar() {
        return (char) ('A' + new Random().nextInt(26));
    }

    private int generateDigit() {
        return new Random().nextInt(10);
    }
}
