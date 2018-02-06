package com.possemeeg.project2017.webdoor.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import java.io.IOException;
import java.net.URL;

@Configuration
@EnableConfigurationProperties({HazelcastProperties.class})
@EnableHazelcastHttpSession
public class HazelcastConfig {

  private final HazelcastProperties hazelcastProperties;

  public HazelcastConfig(HazelcastProperties hazelcastProperties) {
    this.hazelcastProperties = hazelcastProperties;
  }

  @Bean
  public HazelcastInstance hazelcastInstance() throws IOException {
    URL configUrl = hazelcastProperties.getConfig().getURL();
    ClientConfig clientConfig = (new XmlClientConfigBuilder(configUrl)).build();
    return HazelcastClient.newHazelcastClient(clientConfig);
  }
}


