package ua.foxminded.school.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import ua.foxminded.school.config.AppConfig;

public class DatabaseInitializer {

    private final AppConfig config;

    public DatabaseInitializer(AppConfig config) {
        this.config = config;
    }

    public void initialize() {
        try (Connection connection = config.getConnection()) {
            runSqlScript(connection, "sql/init.sql");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private void runSqlScript(Connection connection, String resourcePath) {
        String sql = loadSqlScript(resourcePath);
        try (Statement stmt = connection.createStatement()) {
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute SQL script: " + resourcePath, e);
        }
    }

    private String loadSqlScript(String path) {
        InputStream inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("SQL script not found: " + path);
        }

        try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
