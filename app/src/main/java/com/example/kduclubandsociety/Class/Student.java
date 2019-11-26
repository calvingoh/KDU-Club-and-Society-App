package com.example.kduclubandsociety.Class;

public class Student {
    private String uid; //from firebase
    private String stuID;
    private String token;
    private String name;
    private String email;
    private String clubs;


    public Student(String uid, String token) {
        this.uid = uid;
        this.stuID = stuID;
        this.token = token;
        this.name = name;
        this.email = email;
        this.clubs = clubs;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClubs() {
        return clubs;
    }

    public void setClubs(String clubs) {
        this.clubs = clubs;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
