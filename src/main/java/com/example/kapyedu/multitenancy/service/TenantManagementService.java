package com.example.kapyedu.multitenancy.service;

public interface TenantManagementService {
    void createTenant(String tenantId, String schema);
}
