package com.example.config;

import java.util.Properties;
import com.zaxxer.hikari.HikariConfig;
import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    public static final String WORKDIR = System.getProperty("user.dir");
    private static final Dotenv dotenv = Dotenv.load();

    public final static Properties getProducerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", dotenv.get("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092"));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // Add any other producer-specific properties from your .env file
        // Example: props.put("acks", dotenv.get("KAFKA_PRODUCER_ACKS", "all"));
        return props;
    }

    public final static Properties getConsumerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", dotenv.get("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092"));
        props.put("group.id", dotenv.get("KAFKA_CONSUMER_GROUP_ID", "my-group"));
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        // Add any other consumer-specific properties from your .env file
        // Example: props.put("enable.auto.commit", dotenv.get("KAFKA_CONSUMER_ENABLE_AUTO_COMMIT", "true"));
        return props;
    }

    public static Properties getETHProperties(){
        Properties props = new Properties();
        props.put("eth.apiKey", dotenv.get("ETHERSCAN_TOKEN"));
        return props;
    }

    public static HikariConfig getClickhouseProperties(){
        HikariConfig config = new HikariConfig();
        String url = dotenv.get("CLICKHOUSE_URL");
        String port = dotenv.get("CLICKHOUSE_PORT");
        String database = dotenv.get("CLICKHOUSE_DATABASE");
        config.setJdbcUrl("jdbc:clickhouse://" + url + ":" + port + "/" + database);
        config.setUsername(dotenv.get("CLICKHOUSE_USER"));
        config.setPassword(dotenv.get("CLICKHOUSE_PASSWORD"));
        config.setDriverClassName("com.clickhouse.jdbc.ClickHouseDriver");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return config;
    }
}