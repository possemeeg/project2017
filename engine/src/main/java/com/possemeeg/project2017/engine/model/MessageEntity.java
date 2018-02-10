package com.possemeeg.project2017.engine.model;

import com.possemeeg.project2017.shared.model.Message;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class MessageEntity {
    @Id
    private long id;
    private String message;
    private String sender;

    public MessageEntity() {
    }

    private MessageEntity(long id, String message, String sender) {
        this.id = id;
        this.message = message;
        this.sender = sender;
    }

    public static MessageEntity valueOf(Message message) {
        return new MessageEntity(message.getId(), message.getMessage(), message.getSender());
    }

    public Message toMessage() {
        return Message.forAll(id, message, sender);
    }

    public Message toMessageForUser(String user) {
        return Message.forUser(id, message, sender, user);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

