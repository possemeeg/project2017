package com.possemeeg.project2017.webdoor.controller;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import com.possemeeg.project2017.shared.model.Message;
import com.possemeeg.project2017.shared.reference.Names;
import com.possemeeg.project2017.webdoor.model.Greeting;
import com.possemeeg.project2017.webdoor.model.HelloMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class GreetingController {
  private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);
  private final IList<Message> messages;

  @Autowired
  public GreetingController(HazelcastInstance hazelcastInstance) {
    messages = hazelcastInstance.getList(Names.MESSAGES);
  }

  @MessageMapping("/hello")
  public void greeting(HelloMessage message, Principal user) throws Exception {
    Message newMessage = message.isForAll() ?
      Message.forAll(message.getMessage(), user.getName()) :
      Message.forUser(message.getMessage(), user.getName(), message.getRecipient());
    messages.add(newMessage);
  }

  @SubscribeMapping("/personal.greetings")
  public Collection<Greeting> userUserSubscribed(Principal principal) {
    String user = principal.getName();
    return messages.stream().filter(message -> message.isForUser(user)).map(Greeting::fromMessage).collect(Collectors.toList());
  }
}
