package com.possemeeg.project2017.engine.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "localuser")
public class LocalUserEntity {
    @Id
    private String username;
    private String name;

    public LocalUserEntity(String username, String name) {
        this.username = username;
        this.name = name;
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

