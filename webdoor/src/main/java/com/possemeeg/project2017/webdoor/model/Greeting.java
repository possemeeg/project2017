package com.possemeeg.project2017.webdoor.model;

import com.possemeeg.project2017.shared.model.Message;

public class Greeting {

    private long id;
    private String message;
    private String user;

    public Greeting() {
    }

    public Greeting(long id, String message, String user) {
        this.id = id;
        this.message = message;
        this.user = user;
    }

    public static Greeting fromMessage(Message message) {
        return new Greeting(message.getId(), message.getMessage(), message.getSender());
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }
}
