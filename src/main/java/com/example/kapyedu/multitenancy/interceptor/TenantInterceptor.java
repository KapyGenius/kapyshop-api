package com.example.kapyedu.multitenancy.interceptor;

import com.example.kapyedu.multitenancy.util.TenantContext;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Component
public class TenantInterceptor implements WebRequestInterceptor {
    @Override
    public void preHandle(WebRequest request) throws Exception {
        String tenantId = null;
        if(request.getHeader("X-TENANT-ID") != null) {
            tenantId = request.getHeader("X-TENANT-ID");
        } else {
            tenantId = ((ServletWebRequest) request).getRequest().getServerName().split("\\.")[0];
        }
        TenantContext.setTenantId(tenantId);
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
    }
}
