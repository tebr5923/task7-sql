package com.foxminded.generator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface Generator<T> {
    List<T> generate(int size) throws IOException, URISyntaxException;
}
