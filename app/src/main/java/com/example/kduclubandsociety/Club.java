package com.example.kduclubandsociety;

public class Club {
    private int id;
    private String name;
    private String description;
    private int maxNum;
    private String meeting;

    public Club(int id, String name, String description, int maxNum, String meeting) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxNum = maxNum;
        this.meeting = meeting;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }
}
