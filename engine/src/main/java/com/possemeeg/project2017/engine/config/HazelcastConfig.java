package com.possemeeg.project2017.engine.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;

@Configuration
@EnableConfigurationProperties({HazelcastProperties.class})
public class HazelcastConfig {

  @Bean
  public HazelcastInstance hazelcastInstance(HazelcastProperties hazelcastProperties) throws IOException {
    URL configUrl = hazelcastProperties.getConfig().getURL();
    Config config = (new XmlConfigBuilder(configUrl)).build();
    return Hazelcast.newHazelcastInstance(config);
  }
}


