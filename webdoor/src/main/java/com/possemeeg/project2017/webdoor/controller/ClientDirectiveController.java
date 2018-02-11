package com.possemeeg.project2017.webdoor.controller;

import com.hazelcast.core.*;
import com.possemeeg.project2017.shared.model.Directive;
import com.possemeeg.project2017.shared.reference.Names;
import com.possemeeg.project2017.webdoor.model.ClientDirective;
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
public class ClientDirectiveController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDirectiveController.class);
    private final HazelcastInstance hazelcastInstance;
    private final IMap<Long,Directive> broadcasts;
    private final IAtomicLong nextId;

    @Autowired
    public ClientDirectiveController(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.broadcasts = hazelcastInstance.getMap(Names.BROADCAST_MESSAGE_MAP);
        this.nextId = hazelcastInstance.getAtomicLong(Names.MESSAGE_ID_GENERATOR);
    }

    @MessageMapping("/senddirective")
    public void sendDirective(ClientDirective directive, Principal sender) throws Exception {
        long newId = nextId.incrementAndGet();
        if (directive.isForAll()) {
            broadcasts.put(newId, Directive.forAll(newId, directive.getTextContent(), sender.getName()));
        } else {
            IMap<Long,Directive> userMap = hazelcastInstance.getMap(Names.mapNameForUser(directive.getRecipient()));
            userMap.put(newId, Directive.forUser(newId, directive.getTextContent(), sender.getName(), directive.getRecipient()));
        }
    }

    @SubscribeMapping("/personal.directives")
    public Collection<ClientDirective> userUserSubscribed(Principal principal) {
        String recipient = principal.getName();
        LOGGER.info("User subscribed for greetings {}", recipient);
        IMap<Long,Directive> userMap = hazelcastInstance.getMap(Names.mapNameForUser(recipient));
        return Stream.concat(userMap.values().stream(), broadcasts.values().stream()).map(ClientDirective::fromDirective).collect(Collectors.toList());
    }
}
