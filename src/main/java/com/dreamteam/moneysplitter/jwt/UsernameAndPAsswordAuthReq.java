package com.dreamteam.moneysplitter.jwt;

public class UsernameAndPAsswordAuthReq {
    private String username;
    private String password;

    public UsernameAndPAsswordAuthReq() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
