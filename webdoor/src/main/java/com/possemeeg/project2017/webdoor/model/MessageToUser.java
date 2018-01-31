package com.possemeeg.project2017.webdoor.model;

import com.possemeeg.project2017.shared.model.Message;

public class MessageToUser {

  private String id;
  private String message;
  private String user;

  public MessageToUser() {
  }

  public MessageToUser(String id, String message, String user) {
    this.id = id;
    this.message = message;
    this.user = user;
  }

  public static MessageToUser fromMessage(Message message) {
    return new MessageToUser("id", message.getMessage(), message.getSender());
  }

  public String getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }

  public String getUser() {
    return user;
  }
}
