package com.cg.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class RoleBasedSuccessHandler implements AuthenticationSuccessHandler {

    private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // 1) Honor an explicit returnTo parameter if safe
        String returnTo = request.getParameter("returnTo");
        if (isSafeRelativePath(returnTo)) {
            response.sendRedirect(returnTo);
            return;
        }

        // 2) Else, honor the original saved request
        SavedRequest saved = requestCache.getRequest(request, response);
        if (saved != null && saved.getRedirectUrl() != null) {
            response.sendRedirect(saved.getRedirectUrl());
            return;
        }

        // 3) Else, fallback by role
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else {
            response.sendRedirect("/");
        }
    }

    private boolean isSafeRelativePath(String url) {
        if (url == null || url.isBlank()) return false;
        if (!url.startsWith("/")) return false;
        String lower = url.toLowerCase();
        return !(lower.startsWith("http://") || lower.startsWith("https://"));
    }
}