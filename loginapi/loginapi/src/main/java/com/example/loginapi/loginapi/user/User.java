package com.example.loginapi.loginapi.user;



//@Entity
//@Table(name = "users_info")
public class User {
    private int id;
    private String email;
    private String password;

    public User() {}
    public User(String email,String password,int id) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
