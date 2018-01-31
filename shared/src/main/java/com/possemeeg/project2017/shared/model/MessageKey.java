package com.possemeeg.project2017.shared.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MessageKey implements DataSerializable, Comparable<MessageKey> {
    public long id;
    public long created;

    public MessageKey() {
    }

    private MessageKey(long id, long created) {
        this.id = id;
        this.created = created;
    }

    public static MessageKey create(long id) {
        return new MessageKey(id, LocalDateTime.now().atOffset(ZoneOffset.UTC).toEpochSecond());
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(id);
        out.writeLong(created);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        id = in.readLong();
        created = in.readLong();
    }

    @Override
    public int compareTo(MessageKey other) {
        return compare(this, other);
    }

    public static int compare(MessageKey left, MessageKey right) {
        return Long.compare(left.created,right.created);
    }
}

