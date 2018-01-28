package com.possemeeg.project2017.webdoor.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "application.auth")
public class AuthProperties {
  private Resource users;

  public Resource getUsers() {
    return this.users;
  }

  public void setUsers(Resource users) {
    this.users = users;
  }
}
