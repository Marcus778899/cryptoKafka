package com.example.db;

import java.util.Properties;
import com.example.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.HashMap;
import java.sql.ResultSetMetaData;


public class ClickhouseDriver {

    private static ClickhouseDriver instance;
    private static HikariDataSource dataSource;

    private ClickhouseDriver(){
        Properties props = Config.getClickhouseProperties();

        String url = String.format(
            "jdbc:ch://%s:%s/%s",
            props.getProperty("clickhouse.url"),
            props.getProperty("clickhouse.port"),
            props.getProperty("clickhouse.db")
        );

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(props.getProperty("clickhouse.user"));
        config.setPassword(props.getProperty("clickhouse.password"));

        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);

        dataSource = new HikariDataSource(config);
    }

    public static synchronized ClickhouseDriver getInstance(){
        if (instance == null) {
            instance = new ClickhouseDriver();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public List<Map<String, Object>> selectQuery(String sql) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        }
        return resultList;
    }

    // Insert, Update, Delete operations
    public void executeQuery(String sql) throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public static void main(String[] args) {
        try{
            ClickhouseDriver driver = ClickhouseDriver.getInstance();
            List<Map<String, Object>> rows = driver.selectQuery("SELECT now() as ts");

            for (Map<String, Object> row : rows) {
                System.out.println(row);
            }

            driver.shutdown();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
}
