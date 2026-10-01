package com.hcmute.bookstore.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection_24162056 {
    private static final String CONFIG_FILE = "application.properties";
    private static final Properties PROPERTIES = loadProperties();

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Không tìm thấy SQL Server JDBC Driver", exception);
        }
    }

    private DatabaseConnection_24162056() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                requiredProperty("database.url"),
                requiredProperty("database.username"),
                requiredProperty("database.password"));
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection_24162056.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Khong tim thay " + CONFIG_FILE);
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Khong doc duoc " + CONFIG_FILE, exception);
        }
    }

    private static String requiredProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Thieu cau hinh: " + key);
        }
        return value.trim();
    }
}
