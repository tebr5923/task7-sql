package com.foxminded;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String PROP_FILE_NAME = "db.properties";
    private static final Config INSTANCE = new Config();

    private final Properties properties = new Properties();

    private Config() {
        try (InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(PROP_FILE_NAME)) {
            if (inputStream == null) {
                throw new FileNotFoundException("file " + PROP_FILE_NAME + " not found");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public String value(String key) {
        return this.properties.getProperty(key);
    }
}
