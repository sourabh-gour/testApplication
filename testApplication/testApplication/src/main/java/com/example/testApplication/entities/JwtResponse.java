package com.example.testApplication.entities;

public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // Getters
    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getType() {
        return type;
    }
}
