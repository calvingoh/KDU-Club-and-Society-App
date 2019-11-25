package com.example.kduclubandsociety.Class;

import java.util.Comparator;

public class Announcement {
    private String date;
    private String title;
    private String body;
    private String username;
    private String clubIcon;
    private String clubName;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClubIcon() {
        return clubIcon;
    }

    public void setClubIcon(String clubIcon) {
        this.clubIcon = clubIcon;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public static Comparator<Announcement> DateComparator = new Comparator <Announcement>(){
        public int compare (Announcement n1, Announcement n2){
            String Notification1 = n1.getDate();
            String Notification2 = n2.getDate();

            return Notification1.compareTo(Notification2);
        }
    };
}


