package com.possemeeg.project2017.shared.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Message implements DataSerializable {

  private String message;
  private String sender;
  private List<String> recipients;

  public Message() {
  }
  private Message(String message, String sender, List<String> recipients) {
    this.message = message;
    this.sender = sender;
    this.recipients = recipients;
  }
  public static Message forUsers(String message, String sender, List<String> recipients) {
    Preconditions.checkNotNull(message);
    Preconditions.checkNotNull(sender);
    Preconditions.checkNotNull(recipients);
    Preconditions.checkArgument(!recipients.isEmpty());
    return new Message(message, sender, ImmutableList.copyOf(recipients));
  }
  public static Message forUser(String message, String sender, String recipient) {
    Preconditions.checkNotNull(message);
    Preconditions.checkNotNull(sender);
    Preconditions.checkNotNull(recipient);
    return new Message(message, sender, ImmutableList.of(recipient));
  }
  public static Message forAll(String message, String sender) {
    Preconditions.checkNotNull(message);
    Preconditions.checkNotNull(sender);
    return new Message(message, sender, Collections.<String>emptyList());
  }

  public String getMessage() {
    return message;
  }
  public String getSender() {
    return sender;
  }
  public List<String> getRecipients() {
    return recipients;
  }
  public boolean isForAll() {
    return recipients.isEmpty();
  }
  public boolean isForUser(String user) {
    return isForAll() || recipients.contains(user);
  }
  
  @Override
  public void writeData(ObjectDataOutput out) throws IOException {
    out.writeUTF(message);
    out.writeUTF(sender);
    out.writeUTFArray(recipients.toArray(new String[recipients.size()]));
  }

  @Override
  public void readData(ObjectDataInput in) throws IOException {
    message = in.readUTF();
    sender = in.readUTF();
    recipients = ImmutableList.copyOf(in.readUTFArray());
  }
}
