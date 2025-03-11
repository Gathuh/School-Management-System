package com.mani.schoolmanagement.model;

public class AuthenticationResponse {
    private String token;
    public AuthenticationResponse(String token){
        this.token=token;
    }
    public String getToken(){
        return token;
    }
}
