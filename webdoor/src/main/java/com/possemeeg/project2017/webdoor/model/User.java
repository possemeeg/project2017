package com.possemeeg.project2017.webdoor.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class User {
  private final static Logger LOGGER = LoggerFactory.getLogger(User.class);
  public String user;
  public String password;
  public String role;

  public static List<User> listFromResource(Resource users) {
    ObjectMapper objectMapper = new ObjectMapper();
    TypeFactory typeFactory = objectMapper.getTypeFactory();
    try {
      return objectMapper.readValue(users.getInputStream(), typeFactory.constructCollectionType(List.class, User.class));
    } catch (IOException e) {
      LOGGER.warn("Unable to open users resource");
    }
    return Collections.emptyList();
  }
}
