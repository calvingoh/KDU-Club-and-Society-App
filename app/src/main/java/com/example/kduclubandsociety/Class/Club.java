package com.example.kduclubandsociety.Class;

import java.net.URI;

public class Club {
    private Integer id;
    private String name;
    private String description;
    private Integer maxNum;
    private String meeting;
    private String image;

    public Club(int id, String name, String description, int num, String meeting, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxNum = maxNum;
        this.meeting = meeting;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Club(){}
}
