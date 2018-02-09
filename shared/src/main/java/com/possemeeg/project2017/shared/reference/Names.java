package com.possemeeg.project2017.shared.reference;

public class Names {
  public static final String MESSAGES = "messages";
  public static final String MESSAGE_ID_GENERATOR = "idgenerator:messages";
  public static final String BROADCAST_MESSAGE_MAP = "map:messages:broadcast";
  public static final String USER_MESSAGE_MAP_PREFIX = "map:messages:user:";

  private Names() {
  }

  public static String mapNameForUser(String user) {
      return nameForUser(USER_MESSAGE_MAP_PREFIX, user);
  }

  private static String nameForUser(String prefix, String user) {
      return USER_MESSAGE_MAP_PREFIX + user;
  }
}
