package com.example.smartbank;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private int id;
    private Boolean found;

    public int getUserid() {
        return id;
    }

    public void setUserid(int id) {
        this.id = id;
    }

    public Boolean getFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }
}
