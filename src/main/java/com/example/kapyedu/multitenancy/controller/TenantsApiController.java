package com.example.kapyedu.multitenancy.controller;

import com.example.kapyedu.multitenancy.service.TenantManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/settings")
public class TenantsApiController {
    @Autowired
    private TenantManagementService tenantManagementService;

    @PostMapping("/tenants")
    public ResponseEntity <Void> createTenant(@RequestParam String tenantId, @RequestParam String schema) {
        this.tenantManagementService.createTenant(tenantId, schema);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}



