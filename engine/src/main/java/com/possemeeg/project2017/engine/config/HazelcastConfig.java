package com.possemeeg.project2017.engine.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.config.MapIndexConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import java.io.IOException;
import java.net.URL;

@Configuration
@EnableConfigurationProperties({HazelcastProperties.class})
public class HazelcastConfig {

  @Bean
  public HazelcastInstance hazelcastInstance(HazelcastProperties hazelcastProperties) throws IOException {

      URL configUrl = hazelcastProperties.getConfig().getURL();
      Config config = (new XmlConfigBuilder(configUrl)).build();

      MapAttributeConfig attributeConfig = new MapAttributeConfig()
          .setName(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
          .setExtractor(PrincipalNameExtractor.class.getName());

      config.getMapConfig(HazelcastSessionRepository.DEFAULT_SESSION_MAP_NAME) 
          .addMapAttributeConfig(attributeConfig)
          .addMapIndexConfig(new MapIndexConfig(
                      HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));

      return Hazelcast.newHazelcastInstance(config);
  }
}


