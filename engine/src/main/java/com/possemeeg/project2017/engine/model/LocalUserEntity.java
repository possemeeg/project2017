package com.possemeeg.project2017.engine.model;

import com.possemeeg.project2017.shared.model.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "localuser")
public class LocalUserEntity {
    @Id
    private String username;
    private String name;
    private String password;

    public LocalUserEntity() {
    }

    public LocalUserEntity(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public static LocalUserEntity valueOf(User user) {
        return new LocalUserEntity(user.getUsername(), user.getName(), user.getPassword());
    }

    public User toUser() {
        return new User(username, name, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

