package com.example.snfin.models;

public class UsuarioLoginRequest {

    private String username;
    private String password;

    public UsuarioLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}