package com.possemeeg.project2017.shared.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Directive implements DataSerializable {

    private long id;
    private String textContent;
    private String sender;
    private List<String> recipients;

    public Directive() {
    }
    private Directive(long id, String textContent, String sender, List<String> recipients) {
        this.id = id;
        this.textContent = textContent;
        this.sender = sender;
        this.recipients = recipients;
    }
    public static Directive forUsers(long id, String textContent, String sender, List<String> recipients) {
        Preconditions.checkNotNull(textContent);
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(recipients);
        Preconditions.checkArgument(!recipients.isEmpty());
        return new Directive(id, textContent, sender, ImmutableList.copyOf(recipients));
    }
    public static Directive forUser(long id, String textContent, String sender, String recipient) {
        Preconditions.checkNotNull(textContent);
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(recipient);
        return new Directive(id, textContent, sender, ImmutableList.of(recipient));
    }
    public static Directive forAll(long id, String textContent, String sender) {
        Preconditions.checkNotNull(textContent);
        Preconditions.checkNotNull(sender);
        return new Directive(id, textContent, sender, Collections.<String>emptyList());
    }

    public long getId() {
        return id;
    }
    public String getTextContent() {
        return textContent;
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
        out.writeLong(id);
        out.writeUTF(textContent);
        out.writeUTF(sender);
        out.writeUTFArray(recipients.toArray(new String[recipients.size()]));
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        id = in.readLong();
        textContent = in.readUTF();
        sender = in.readUTF();
        recipients = ImmutableList.copyOf(in.readUTFArray());
    }
}

