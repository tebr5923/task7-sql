package com.foxminded.my_reader;

//because the interface reader already exists. I can't think better name
public interface MyReader {
    String readString();

    int readInt();

    void close();
}
