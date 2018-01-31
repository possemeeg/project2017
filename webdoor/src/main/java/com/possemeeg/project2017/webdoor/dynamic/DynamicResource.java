package com.possemeeg.project2017.webdoor.dynamic;

import com.possemeeg.project2017.webdoor.config.properties.DynamicProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@EnableConfigurationProperties({DynamicProperties.class})
public class DynamicResource {
  private static int count = 0;
  private static List<String> lines;
  private int id;
  private String line;

  public DynamicResource(@Autowired DynamicProperties dynamicProperties) {
    this.id = nextCount();
    this.line = getLine(dynamicProperties, id);
  }

  private synchronized static int nextCount() {
    return count++;
  }

  private synchronized static String getLine(DynamicProperties dynamicProperties, int count) {
    if (lines == null) {
      Resource resource = dynamicProperties.getFile();
      try (Reader reader = new InputStreamReader(resource.getInputStream()); BufferedReader lineReader = new BufferedReader(reader)) {
        lines = lineReader.lines().collect(Collectors.toList());
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
      }
    }
    return lines.get(count % lines.size());
  }

  public String name() {
    return toString();
  }
  public String line() {
    return line;
  }

  @Override
  public String toString() {
    return String.format("DynamicResource %d", id);
  }
}
