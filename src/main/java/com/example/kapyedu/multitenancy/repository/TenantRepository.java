package com.example.kapyedu.multitenancy.repository;

import com.example.kapyedu.multitenancy.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, String> {
    //@Query("select t from Tenant t where  t.tenantId = :tenantId")
    //Optional<Tenant> findTenantByTenantId(@Param("tenantId") String tenantId);
    Optional<Tenant> findTenantByTenantId(String tenantId);
}
