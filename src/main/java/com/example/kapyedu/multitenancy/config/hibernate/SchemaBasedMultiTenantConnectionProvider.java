package com.example.kapyedu.multitenancy.config.hibernate;

import com.example.kapyedu.multitenancy.entity.Tenant;
import com.example.kapyedu.multitenancy.repository.TenantRepository;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SchemaBasedMultiTenantConnectionProvider implements MultiTenantConnectionProvider {
    private final transient DataSource datasource;
    private final transient TenantRepository tenantRepository;

    private final Long maximumSize;
    private final Integer expireAfterAccess;

    private transient LoadingCache<String, String> tenantSchemas;

    @PostConstruct
    private void createCache() {
        Caffeine<Object, Object> tenantsCacheBuilder = Caffeine.newBuilder();
        if (maximumSize != null) {
            tenantsCacheBuilder.maximumSize(maximumSize);
        }
        if (expireAfterAccess != null) {
            tenantsCacheBuilder.expireAfterAccess(expireAfterAccess, TimeUnit.MINUTES);
        }

        tenantSchemas = tenantsCacheBuilder.build(new CacheLoader<String, String>() {
                    public String load(String key) {
                        Tenant tenant = tenantRepository.findTenantByTenantId(key)
                                .orElseThrow(() -> new RuntimeException("No such tenant: " + key));
                        return tenant.getSchema();
                    }
                });
    }

    @Autowired
    public SchemaBasedMultiTenantConnectionProvider(
            DataSource datasource,
            TenantRepository tenantRepository,
            @Value("${multitenancy.schema-cache.maximumSize:1000}")
            Long maximumSize,
            @Value("${multitenancy.schema-cache.expireAfterAccess:10}")
            Integer expireAfterAccess) {
        this.datasource = datasource;
        this.tenantRepository = tenantRepository;
        this.maximumSize = maximumSize;
        this.expireAfterAccess = expireAfterAccess;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return datasource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String s) throws SQLException {
        log.info("Get Connection for Tenant {}", s);
        String tenantSchema;
        tenantSchema = tenantSchemas.get(s);
        final Connection connection = getAnyConnection();
        connection.setSchema(tenantSchema);
        return connection;
    }

    @Override
    public void releaseConnection(String s, Connection connection) throws SQLException {
        log.info("Release connection for tenant {}", s);
        connection.setSchema(null);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return MultiTenantConnectionProvider.class.isAssignableFrom(aClass);
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        if ( MultiTenantConnectionProvider.class.isAssignableFrom(aClass) ) {
            return (T) this;
        } else {
            throw new UnknownUnwrapTypeException( aClass );
        }
    }
}
