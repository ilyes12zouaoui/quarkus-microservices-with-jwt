package tn.ilyeszouaoui.config;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

public class PostgresTestContainerConfiguration implements QuarkusTestResourceLifecycleManager {

    private final PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("dbname")
            .withUsername("dbname")
            .withPassword("dbname");

    @Override
    public Map<String, String> start() {
        dbContainer.start();
        HashMap<String, String> hashMap= new HashMap<>();
        hashMap.put("quarkus.datasource.jdbc.url", dbContainer.getJdbcUrl());
        return hashMap;
    }

    @Override
    public void stop() {
        dbContainer.stop();
    }

}
