package com.foxminded.dao;

import com.foxminded.reader.ResourceFileReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S106") //dont use logger in this task
public class DBFactory {
    private static final String CREATE_TABLES_FILE_NAME = "schema.sql";
    private static final String DROP_TABLES_FILE_NAME = "drop.sql";

    private final ConnectionProvider connectionProvider;
    private final String createTablesFileName;
    private final String dropTablesFileName;

    public DBFactory(ConnectionProvider connectionProvider) {
        this(connectionProvider, CREATE_TABLES_FILE_NAME, DROP_TABLES_FILE_NAME);
    }

    public DBFactory(ConnectionProvider connectionProvider, String createTablesFileName, String dropTablesFileName) {
        this.connectionProvider = connectionProvider;
        this.createTablesFileName = createTablesFileName;
        this.dropTablesFileName = dropTablesFileName;
    }

    public void createTables() {
        executeScript(createTablesFileName, "CREATE");
    }

    public void dropTables() {
        executeScript(dropTablesFileName, "DROP");
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

    private void executeQuery(String script, String message) {
        try (final Connection connection = connectionProvider.getConnection();
             final Statement statement = connection.createStatement()) {
            statement.execute(script);
            System.out.printf("table %s%n", message);
        } catch (SQLException e) {
            System.err.printf("table NOT %s%n", message);
            throw new IllegalStateException(String.format("cant %s table", message), e);
        }
    }

    private void executeScript(String fileName, String message){
        Optional<String> script = getScript(fileName);
        if (script.isPresent()) {
            executeQuery(script.get(), message);
        } else {
            System.err.printf("tables NOT %s - EMPTY SCRIPT%n", message);
        }
    }
}
