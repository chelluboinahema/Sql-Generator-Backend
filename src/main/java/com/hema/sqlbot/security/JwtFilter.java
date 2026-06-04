package com.hema.sqlbot.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final AppUserDetailsService userService;

    public JwtFilter(
            JwtService jwtService,
            AppUserDetailsService userService) {

        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException,
            IOException {

        String auth =
                request.getHeader(
                        "Authorization"
                );

        if (
                auth == null
                        ||
                        !auth.startsWith(
                                "Bearer "
                        )
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String token =
                auth.substring(
                        7
                );

        if (
                jwtService.isValid(
                        token
                )
        ) {

            String email =
                    jwtService.extractEmail(
                            token
                    );

            UserDetails details =
                    userService.loadUserByUsername(
                            email
                    );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            details,
                            null,
                            details.getAuthorities()
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(
                            authentication
                    );
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}