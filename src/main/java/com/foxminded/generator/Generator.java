package com.foxminded.generator;

import java.util.List;

public interface Generator<T> {
    List<T> generate(int size);
}
