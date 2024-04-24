package com.wallet.entitis;

public class LoginRequest {
    private String username;
    private String password;

    // Add constructors, getters, and setters as needed

    // Example constructor
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Example getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
