package com.possemeeg.project2017.shared.reference;

public class MessageMapNames {
  public static String MESSAGES_GLOBAL_NAME = "messages-global";
  private static String MESSAGES_PERSONAL_PREFIX = "messages-personal-";

  public static String userMapName(String username) {
      return MESSAGES_PERSONAL_PREFIX + username;
  }
}
