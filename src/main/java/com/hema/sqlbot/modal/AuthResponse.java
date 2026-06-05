package com.hema.sqlbot.modal;

public class AuthResponse {

    private String token;

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    private Role userRole;

    public AuthResponse() {
    }

    public AuthResponse(
            String token ,Role userRole
    ) {
        this.token = token;
        this.userRole=userRole;
    }

    public String getToken() {
        return token;
    }

    public void setToken(
            String token
    ) {
        this.token = token;
    }
}