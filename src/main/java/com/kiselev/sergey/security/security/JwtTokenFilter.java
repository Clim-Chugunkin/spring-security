package com.kiselev.sergey.security.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
            ServletException {

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String jwt = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
            if (StringUtils.hasText(jwt)) {
                if (jwtTokenProvider.isTokenValid(jwt)) {
                    Authentication authentication = jwtTokenProvider.getAuth(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);

            this.resetAuthenticationAfterRequest();
        } catch (ExpiredJwtException eje) {
            System.out.println("token has inspired");
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }
    }

    private void resetAuthenticationAfterRequest() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
