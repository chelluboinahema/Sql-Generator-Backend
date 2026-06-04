package com.hema.sqlbot.security;

import com.hema.sqlbot.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService
        implements UserDetailsService {

    private final UserRepository repository;

    public AppUserDetailsService(
            UserRepository repository) {

        this.repository = repository;
    }
    @Override
    public UserDetails loadUserByUsername(
            String email
    ) {

        com.hema.sqlbot.modal.User user =

                repository
                        .findByEmail(email)

                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                "User not found"
                                        )
                        );

        return org.springframework.security.core.userdetails.User

                .builder()

                .username(
                        user.getEmail()
                )

                .password(
                        user.getPassword()
                )

                .authorities(
                        "ROLE_" +
                                user.getRole().name()
                )

                .build();
    }
}