package com.possemeeg.project2017.webdoor.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "application.dynamic")
public class DynamicProperties {
  private Resource file;

  public Resource getFile() {
    return this.file;
  }

  public void setFile(Resource file) {
    this.file = file;
  }
}

