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

        // 公开接口无需鉴权
        if (isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 所有非公开接口必须携带有效 token（包括 GET 请求）
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或会话已过期\",\"data\":null}");
            return;
        }

        LoginResponse loginInfo = AuthService.getByToken(token);
        if (loginInfo == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或会话已过期\",\"data\":null}");
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