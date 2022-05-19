package com.example.kapyedu.multitenancy.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantContext {
    private TenantContext() {
    }

    private static InheritableThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static  void setTenantId(String tenantId) {
        log.debug("setting tenant id" + tenantId);
        currentTenant.set(tenantId);
    }

    public static String getTenantId() {
        return  currentTenant.get();
    }

    public  static  void clear() {
        currentTenant.remove();
    }
}
