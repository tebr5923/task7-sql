package com.foxminded.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

@FunctionalInterface
public interface Reader {
    Stream<String> read(String fileName) throws IOException, URISyntaxException;
}
