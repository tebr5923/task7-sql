package com.foxminded.generator;

import com.foxminded.domain.Group;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupGeneratorTest {
    private static final long seed = 10;

    @Test
    void generate_shouldReturnGroupList() {
        Group groupPG30 = new Group();
        groupPG30.setName("PG-30");
        Group groupES78 = new Group();
        groupES78.setName("ES-78");
        Group groupVC39 = new Group();
        groupVC39.setName("VC-39");
        List<Group> expected = Arrays.asList(groupPG30, groupES78, groupVC39);

        List<Group> actual = new GroupGenerator(new Random(seed)).generate(3);

        assertEquals(expected, actual);
    }
}
