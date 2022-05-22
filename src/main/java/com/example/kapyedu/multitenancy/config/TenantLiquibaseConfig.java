package com.example.kapyedu.multitenancy.config;

import com.example.kapyedu.multitenancy.config.liquibase.DynamicSchemaBasedMultiTenantSpringLiquibase;
import com.example.kapyedu.multitenancy.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.sql.DataSource;

@Lazy(false)
@Configuration
@ConditionalOnProperty(name = "multitenancy.tenant.liquibase.enabled", havingValue = "true", matchIfMissing = true)
public class TenantLiquibaseConfig {

    @Bean
    public DynamicSchemaBasedMultiTenantSpringLiquibase tenantLiquibase(
            TenantRepository masterTenantRepository,
            DataSource dataSource,
            @Qualifier("tenantLiquibaseProperties")
            LiquibaseProperties liquibaseProperties) {
        return new DynamicSchemaBasedMultiTenantSpringLiquibase(masterTenantRepository, dataSource, liquibaseProperties);
    }

}
