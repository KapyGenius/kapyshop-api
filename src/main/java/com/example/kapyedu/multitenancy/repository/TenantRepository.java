package com.example.kapyedu.multitenancy.repository;

import com.example.kapyedu.multitenancy.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, String> {
    //@Query("select t from Tenant t where  t.tenantId = :tenantId")
    //Optional<Tenant> findTenantByTenantId(@Param("tenantId") String tenantId);
    Optional<Tenant> findTenantByTenantId(String tenantId);
}
