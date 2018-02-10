package com.possemeeg.project2017.shared.reference;

import com.google.common.base.Preconditions;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Names {
  public static final String MESSAGES = "messages";
  public static final String MESSAGE_ID_GENERATOR = "automiclong:messageid";
  public static final String MESSAGE_MAP_PREFIX = "map:messages:";
  public static final String BROADCAST_MESSAGE_MAP = MESSAGE_MAP_PREFIX + "broadcast";
  public static final String USER_MESSAGE_MAP_PREFIX = MESSAGE_MAP_PREFIX + "user:";
  private static final Pattern IS_USER_MESSAGE_MAP = Pattern.compile("^" + USER_MESSAGE_MAP_PREFIX + "(.+)");

  private Names() {
  }

  public static String mapNameForUser(String user) {
      return USER_MESSAGE_MAP_PREFIX + user;
  }

  public static String userFromMapName(String mapname) {
      Matcher match = IS_USER_MESSAGE_MAP.matcher(mapname);
      Preconditions.checkArgument(match.matches(), String.format("Invalid map name for user: %s", mapname));
      return match.group(1);
  }
}
