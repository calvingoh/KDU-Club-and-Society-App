package com.example.kduclubandsociety;

public class Club {
    private Integer id;
    private String name;
    private String description;
    private Integer maxNum;
    private String meeting;

    public Club() {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxNum = maxNum;
        this.meeting = meeting;
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
}
