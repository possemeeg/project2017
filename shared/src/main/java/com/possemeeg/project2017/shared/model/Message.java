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

    public Message() {
    }

    public Message(String sender, String message) {
        this.message = message;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(sender);
        out.writeUTF(message);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        sender = in.readUTF();
        message = in.readUTF();
    }
}

