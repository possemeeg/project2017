package com.possemeeg.project2017.webdoor.model;

import com.google.common.base.Strings;

public class HelloMessage {

    private String message;
    private String recipient;

    public HelloMessage() {
    }

    public HelloMessage(String message, String recipient) {
        this.message = message;
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isForAll() {
      return Strings.isNullOrEmpty(recipient);
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
