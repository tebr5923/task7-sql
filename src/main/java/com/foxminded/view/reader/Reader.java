package com.foxminded.view.reader;

public interface Reader extends AutoCloseable{
    String readString();

    int readInt();

}
