package com.vitaly.dlmanager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MysqlTestContainerConfig {

    @Bean
    @ServiceConnection
    public MySQLContainer<?> mySQLContainer(){
        return new MySQLContainer<>("mysql:8.3.0").withReuse(true);
    }
}
