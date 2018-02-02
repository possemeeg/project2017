package com.possemeeg.project2017.shared.reference;

public class MessageMapNames {
  public static String LOGGED_ON_USERS = "logged-on-users";
  public static String MESSAGES_GLOBAL_NAME = "messages-global";
  public static String PROCESS_ID_GENERATOR = "process-id-generator";
  private static String MESSAGES_PERSONAL_PREFIX = "messages-personal-";

  public static String userMapName(String username) {
      return MESSAGES_PERSONAL_PREFIX + username;
  }
}
