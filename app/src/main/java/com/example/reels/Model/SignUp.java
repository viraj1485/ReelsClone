package com.example.reels.Model;

public class SignUp {
    String email;
    String fullname;
    String Username;
    String passord;
    String uid;

    public SignUp(String email, String fullname, String Username, String passord, String uid) {
        this.email = email;
        this.fullname = fullname;
        this.Username = Username;
        this.passord = passord;
        this.uid=uid;
    }

    public SignUp() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
