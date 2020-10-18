package com.paulok777.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UrlAuthentificationSuccessHandler implements AuthenticationSuccessHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication
    ) throws IOException {
        handle(httpServletRequest, httpServletResponse, authentication);
        clearAuthenticationAttributes(httpServletRequest);
    }

    protected void handle(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (httpServletResponse.isCommitted()) {
            return;
        }

        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, targetUrl);
    }

    protected String determineTargetUrl(final Authentication authentication) {
        Map<String, String> roleTargetMap = new HashMap<>();
        roleTargetMap.put("COMMODITY_EXPERT", "/commodity_expert/products");
        roleTargetMap.put("CASHIER", "/cashier/orders");
        roleTargetMap.put("SENIOR_CASHIER", "/senior_cashier/orders");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority: authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (roleTargetMap.containsKey(authorityName)) {
                return roleTargetMap.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
