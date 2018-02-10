package com.possemeeg.project2017.webdoor.controller;

import com.hazelcast.core.*;
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
import java.util.stream.Stream;

@Controller
public class GreetingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);
    private final HazelcastInstance hazelcastInstance;
    private final IMap<Long,Message> broadcasts;
    private final IAtomicLong nextId;

    @Autowired
    public GreetingController(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.broadcasts = hazelcastInstance.getMap(Names.BROADCAST_MESSAGE_MAP);
        this.nextId = hazelcastInstance.getAtomicLong(Names.MESSAGE_ID_GENERATOR);
    }

    @MessageMapping("/hello")
    public void greeting(HelloMessage message, Principal user) throws Exception {
        long newMessageId = nextId.incrementAndGet();
        if (message.isForAll()) {
            broadcasts.put(newMessageId, Message.forAll(newMessageId, message.getMessage(), user.getName()));
        } else {
            IMap<Long,Message> userMap = hazelcastInstance.getMap(Names.mapNameForUser(message.getRecipient()));
            userMap.put(newMessageId, Message.forUser(newMessageId, message.getMessage(), user.getName(), message.getRecipient()));
        }
    }

    @SubscribeMapping("/personal.greetings")
    public Collection<Greeting> userUserSubscribed(Principal principal) {
        String user = principal.getName();
        LOGGER.info("User subscribed for greetings {}", user);
        IMap<Long,Message> userMap = hazelcastInstance.getMap(Names.mapNameForUser(user));
        return Stream.concat(userMap.values().stream(), broadcasts.values().stream()).map(Greeting::fromMessage).collect(Collectors.toList());
    }
}
