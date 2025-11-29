package com.adil.bridgespero.config;

import com.adil.bridgespero.datasource.RoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    String dsUrl;

    @Value("${spring.datasource.replicaUrl}")
    String replicaUrl;

    @Value("${spring.datasource.username}")
    String dsUsername;

    @Value("${spring.datasource.password}")
    String dsPassword;

    @Bean
    public DataSource writeDataSource() {
        return DataSourceBuilder.create()
                .url(dsUrl)
                .username(dsUsername)
                .password(dsPassword)
                .build();
    }

    @Bean
    public DataSource readDataSource() {
        return DataSourceBuilder.create()
                .url(replicaUrl)
                .username(dsUsername)
                .password(dsPassword)
                .build();
    }

    @Bean
    @Primary
    public DataSource routingDataSource(
            @Qualifier("writeDataSource") DataSource writeDataSource,
            @Qualifier("readDataSource") DataSource readDataSource) {

        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("WRITE", writeDataSource);
        dataSources.put("READ", readDataSource);

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(writeDataSource);

        return routingDataSource;
    }
}
