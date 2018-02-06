package com.possemeeg.project2017.webdoor.model.client;

public class UserChange {
    private String user;
    private boolean online;

    public UserChange() {
    }

    public UserChange(String user, boolean online) {
        this.user = user;
        this.online = online;
    }

    public String getUser() {
        return user;
    }

    public boolean getOnline() {
        return online;
    }
}

