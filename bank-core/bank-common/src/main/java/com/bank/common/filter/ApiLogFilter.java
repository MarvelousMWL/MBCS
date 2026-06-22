package com.bank.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(0)
public class ApiLogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger("API_LOG");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        String method = request.getMethod();
        String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        // 设置 MDC 方便日志分类
        MDC.put("traceId", traceId);
        MDC.put("method", method);
        MDC.put("path", path);

        long start = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = response.getStatus();

            // 根据路径确定业务模块
            String module = detectModule(path);

            if (path.startsWith("/api/")) {
                if (status >= 400) {
                    log.error("[{}][{}] {} {} -> {} ({}ms) [{}]", traceId, module, method, path, status, duration, request.getRemoteAddr());
                } else {
                    log.info("[{}][{}] {} {} -> {} ({}ms)", traceId, module, method, path, status, duration);
                }
            }

            MDC.clear();
        }
    }

    private String detectModule(String path) {
        if (path.contains("/teller/")) return "柜员";
        if (path.contains("/liability/")) return "负债";
        if (path.contains("/customer/")) return "客户";
        if (path.contains("/auth/")) return "认证";
        if (path.contains("/institution")) return "机构";
        if (path.contains("/product")) return "产品";
        if (path.contains("/account")) return "账户";
        if (path.contains("/transaction")) return "交易";
        if (path.contains("/batch")) return "批量";
        if (path.contains("/certificate")) return "大额存单";
        if (path.contains("/notice")) return "通知存款";
        if (path.contains("/transfer")) return "转账";
        return "其他";
    }
}