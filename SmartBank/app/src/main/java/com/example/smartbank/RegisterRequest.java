package com.example.smartbank;

public class RegisterRequest {

    private int id;
    private String email;
    private String password;

    RegisterRequest(){}

    RegisterRequest(String email,String password,int id)
    {
        this.id = id;
        this.email = email;
        this.password = password;
    }
    public int getUserid() {
        return id;
    }

    public void setUserid(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
