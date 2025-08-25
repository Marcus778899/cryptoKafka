package com.example.db;

import com.example.config.Config;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A thread-safe Singleton ClickHouse driver that manages a connection pool.
 */
public class ClickhouseDriver {

    private static volatile ClickhouseDriver instance;
    private final HikariDataSource dataSource;

    /**
     * Private constructor to initialize the connection pool.
     */
    private ClickhouseDriver() {
        
        HikariConfig config = Config.getClickhouseProperties();

        this.dataSource = new HikariDataSource(config);
    }

    /**
     * Returns the singleton instance of the driver.
     * @return The singleton ClickhouseDriver instance.
     */
    public static ClickhouseDriver getInstance() {
        // Use double-checked locking for lazy initialization.
        if (instance == null) {
            synchronized (ClickhouseDriver.class) {
                if (instance == null) {
                    instance = new ClickhouseDriver();
                }
            }
        }
        return instance;
    }

    /**
     * Executes a SQL query and returns the result as a list of maps.
     * Each map represents a row, with column names as keys.
     *
     * @param sql The SQL query to execute.
     * @return A list of maps representing the query result.
     * @throws SQLException if a database access error occurs.
     */
    public List<Map<String, Object>> SelectQuery(String sql) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>(columns);
                for (int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        }
        return resultList;
    }
    /**
     * Executes a SQL query with insert, update, delete
     * 
     * @param sql The SQL query to execute.
     */
    public void execute(String sql) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Closes the underlying connection pool.
     * Should be called on application shutdown.
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
