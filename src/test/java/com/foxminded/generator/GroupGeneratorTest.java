package com.foxminded.generator;

import com.foxminded.domain.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupGeneratorTest {
    @Mock
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        when(mockRandom.nextInt(Mockito.anyInt())).thenReturn(1);
    }

    @Test
    void generate_shouldReturnGroupList() {
        Group group = new Group();
        group.setName("BB-11");
        List<Group> expected = Arrays.asList(group, group, group);

        List<Group> actual = new GroupGenerator(mockRandom).generate(3);

        assertEquals(expected, actual);
    }
}
