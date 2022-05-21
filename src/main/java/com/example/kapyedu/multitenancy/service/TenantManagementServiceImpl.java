package com.example.kapyedu.multitenancy.service;

import com.example.kapyedu.multitenancy.entity.Tenant;
import com.example.kapyedu.multitenancy.repository.TenantRepository;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Collections;


@RequiredArgsConstructor
@Service
public class TenantManagementServiceImpl implements TenantManagementService {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("tenantLiquibaseProperties")
    private final LiquibaseProperties liquibaseProperties;
    private final ResourceLoader resourceLoader;
    private final TenantRepository tenantRepository;

    private static final String VALID_SCHEMA_NAME_REGEXP = "[A-Za-z0-9_]*";

    @Override
    public void createTenant(String tenantId, String schema) {

        // Verify schema string to prevent SQL injection
        if (!schema.matches(VALID_SCHEMA_NAME_REGEXP)) {
            throw new TenantCreationException("Invalid schema name: " + schema);
        }

        try {
            createSchema(schema);
            runLiquibase(dataSource, schema);
        } catch (DataAccessException e) {
            throw new TenantCreationException("Error when creating schema: " + schema, e);
        } catch (LiquibaseException e) {
            throw new TenantCreationException("Error when populating schema: ", e);
        }
        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .schema(schema)
                .build();
        tenantRepository.save(tenant);
    }

    private void createSchema(String schema) {
        jdbcTemplate.execute((StatementCallback<Boolean>) stmt -> stmt.execute("CREATE SCHEMA " + schema));
    }

    private void runLiquibase(DataSource dataSource, String schema) throws LiquibaseException {
        SpringLiquibase liquibase = getSpringLiquibase(dataSource, schema);
        liquibase.afterPropertiesSet();
    }

    protected SpringLiquibase getSpringLiquibase(DataSource dataSource, String schema) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(schema);
        if (liquibaseProperties.getParameters() != null) {
            liquibaseProperties.getParameters().put("schema", schema);
            liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        } else {
            liquibase.setChangeLogParameters(Collections.singletonMap("schema", schema));
        }
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        return liquibase;
    }
}
