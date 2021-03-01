package com.foxminded.dao;

import com.foxminded.reader.ResourceFileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S106")
public class DBFactory {
    private static final String CREATE_TABLES_FILE_NAME = "schema.sql";
    private static final String DROP_TABLES_FILE_NAME = "drop.sql";

    private final String createTablesFileName;
    private final String dropTablesFileName;

    public DBFactory() {
        this(CREATE_TABLES_FILE_NAME, DROP_TABLES_FILE_NAME);
    }

    public DBFactory(String createTablesFileName, String dropTablesFileName) {
        this.createTablesFileName = createTablesFileName;
        this.dropTablesFileName = dropTablesFileName;
    }

    public void createTables() {
        Optional<String> script = getScript(createTablesFileName);
        if (script.isPresent()) {
            executeScript(script.get(), "CREATE");
        } else {
            System.err.println("tables NOT CREATE - EMPTY SCRIPT");
        }
    }

    public void dropTables() {
        Optional<String> script = getScript(dropTablesFileName);
        if (script.isPresent()) {
            executeScript(script.get(), "DROP");
        } else {
            System.err.println("tables NOT DROP - EMPTY SCRIPT");
        }
    }

    private Optional<String> getScript(String fileName) {
        ResourceFileReader resourceFileReader = new ResourceFileReader();
        try {
            String script = resourceFileReader.read(fileName).collect(Collectors.joining());
            return Optional.of(script);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void executeScript(String script, String message) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (final Connection connection = connectionFactory.getConnection();
             final Statement statement = connection.createStatement()) {
            statement.execute(script);
            System.out.printf("table %s%n", message);
        } catch (SQLException e) {
            System.err.printf("table NOT %s%n", message);
            throw new IllegalStateException(String.format("cant %s table", message), e);
        }
    }
}
