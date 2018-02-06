package com.possemeeg.project2017.engine.component;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestListener {
  private final HazelcastInstance hazelcastInstance;
  public RequestListener(@Autowired HazelcastInstance hazelcastInstance) {
    this.hazelcastInstance = hazelcastInstance;
  }
}
