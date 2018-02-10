package com.possemeeg.project2017.shared.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class User implements DataSerializable {
    private String username;
    private String name;
    private String password;

    public User() {
    }

    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(name);
        out.writeUTF(password);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        username = in.readUTF();
        name = in.readUTF();
        password = in.readUTF();
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}

