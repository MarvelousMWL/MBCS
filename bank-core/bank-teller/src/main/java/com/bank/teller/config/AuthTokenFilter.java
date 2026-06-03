package com.bank.teller.config;

import com.bank.teller.application.auth.AuthService;
import com.bank.teller.application.auth.LoginResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class AuthTokenFilter implements Filter {

    private static final String[] PUBLIC_URLS = {
            "/api/teller/auth/login",
            "/api/teller/institution",
            "/api/teller/teller/institution/"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Skip auth for public endpoints and GET requests (read operations)
        if ("GET".equalsIgnoreCase(method) || isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Check token for write operations (POST, PUT, DELETE)
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"\u672a\u767b\u5f55\u6216\u4f1a\u8bdd\u5df2\u8fc7\u671f\",\"data\":null}");
            return;
        }

        LoginResponse loginInfo = AuthService.getByToken(token);
        if (loginInfo == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"\u672a\u767b\u5f55\u6216\u4f1a\u8bdd\u5df2\u8fc7\u671f\",\"data\":null}");
            return;
        }

        request.setAttribute("operator-no", loginInfo.getTellerNo());
        request.setAttribute("operator-institution", loginInfo.getInstitutionNo());
        request.setAttribute("operator-type", loginInfo.getTellerType());

        chain.doFilter(request, response);
    }

    private boolean isPublicUrl(String path) {
        for (String url : PUBLIC_URLS) {
            if (path.startsWith(url)) {
                return true;
            }
        }
        return false;
    }
}