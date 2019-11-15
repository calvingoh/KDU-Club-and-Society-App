package com.example.kduclubandsociety.Class;

public class Student {
    private String uid; //from firebase
    private int id;
    private String token;
    private String name;
    private String email;
    private int[][] clubs;


    public Student(String uid, String token) {
        this.uid = uid;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int[][] getClubs() {
        return clubs;
    }

    public void setClubs(int[][] clubs) {
        this.clubs = clubs;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
