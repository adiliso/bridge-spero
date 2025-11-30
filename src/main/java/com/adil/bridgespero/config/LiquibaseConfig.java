package com.adil.bridgespero.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class LiquibaseConfig {

    @Value("${super-admin.email}")
    private String email;

    @Value("${super-admin.password}")
    private String password;

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog-master.yaml");

        Map<String, String> params;
        params = new HashMap<>();
        params.put("superAdminEmail", email);
        params.put("superAdminPassword", password);

        liquibase.setChangeLogParameters(params);
        return liquibase;
    }
}
