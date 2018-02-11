package com.possemeeg.project2017.engine.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapStore;
import com.hazelcast.core.MapStoreFactory;
import com.possemeeg.project2017.engine.data.DirectiveRepository;
import com.possemeeg.project2017.engine.haz.BroadcastStore;
import com.possemeeg.project2017.engine.haz.DirectiveStore;
import com.possemeeg.project2017.engine.haz.UserStore;
import com.possemeeg.project2017.shared.model.Directive;
import com.possemeeg.project2017.shared.model.User;
import com.possemeeg.project2017.shared.reference.Names;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties({HazelcastProperties.class})
public class HazelcastConfig {

  @Bean
  public HazelcastInstance hazelcastInstance(ApplicationContext applicationContext, HazelcastProperties hazelcastProperties) throws IOException {

      URL configUrl = hazelcastProperties.getConfig().getURL();
      Config config = (new XmlConfigBuilder(configUrl)).build();

      MapAttributeConfig attributeConfig = new MapAttributeConfig()
          .setName(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
          .setExtractor(PrincipalNameExtractor.class.getName());

      config.getMapConfig(HazelcastSessionRepository.DEFAULT_SESSION_MAP_NAME) 
          .addMapAttributeConfig(attributeConfig)
          .addMapIndexConfig(new MapIndexConfig(
                      HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));

      config.getMapConfig(Names.MESSAGE_MAP_PREFIX + "*")
          .getMapStoreConfig()
          .setEnabled(true)
          .setWriteDelaySeconds(10)
          .setWriteBatchSize(1000)
          .setWriteCoalescing(true)
          .setFactoryImplementation((MapStoreFactory<Long, Directive>) (mapName, properties) -> {
              if (Names.BROADCAST_MESSAGE_MAP.equals(mapName)) {
                  return applicationContext.getBean(BroadcastStore.class);
              } else {
                  DirectiveStore store = applicationContext.getBean(DirectiveStore.class);
                  store.setUser(Names.userFromMapName(mapName));
                  return store;
              }
          });

      config.getMapConfig(Names.USER_MAP)
          .getMapStoreConfig()
          .setEnabled(true)
          .setWriteDelaySeconds(10)
          .setWriteBatchSize(1000)
          .setWriteCoalescing(true)
          .setFactoryImplementation((MapStoreFactory<String, User>) (mapName, properties) ->
                  applicationContext.getBean(UserStore.class));

      return Hazelcast.newHazelcastInstance(config);
  }
}


