package com.webtutsplus.ecommerce.dto.user;

public class SignInResponseDto {
    private String status;
    private String token;
    private String userName;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SignInResponseDto(String status, String token) {
        this.status = status;
        this.token = token;
    }

    public SignInResponseDto(String status, String token, String userName) {
        this.status = status;
        this.token = token;
        this.userName = userName;
    }
}
