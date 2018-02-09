package com.possemeeg.project2017.webdoor.component;

import com.hazelcast.core.*;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.MapListener;
import com.possemeeg.project2017.shared.model.Message;
import com.possemeeg.project2017.shared.reference.Names;
import com.possemeeg.project2017.webdoor.model.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class MessageRouter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);
    private final HazelcastInstance hazelcastInstance;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Map<String,String> attachedUserMessages = new ConcurrentHashMap<>();
    private final IMap<Long,Message> broadcasts;

    @Autowired
    public MessageRouter(HazelcastInstance hazelcastInstance, SimpMessagingTemplate simpMessagingTemplate) {
        this.hazelcastInstance = hazelcastInstance;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.broadcasts = hazelcastInstance.getMap(Names.BROADCAST_MESSAGE_MAP);

        Consumer<Greeting[]> broadcastHandler =
            msgs ->
               attachedUserMessages.keySet().stream().forEach(user ->
                       simpMessagingTemplate.convertAndSendToUser(user, "/personal.greetings", msgs));
        broadcasts.addEntryListener(new MessageListener(broadcastHandler), true);
    }

    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        String user = event.getUser().getName(); 
        LOGGER.info("User {} connected", user);
        IMap<Long,Message> userMap = hazelcastInstance.getMap(Names.mapNameForUser(user));
        MessageListener userMessageListener = new MessageListener(msgs -> simpMessagingTemplate.convertAndSendToUser(user, "/personal.greetings", msgs));
        attachedUserMessages.put(user, userMap.addEntryListener(userMessageListener, true));
    }

    private class MessageListener implements MapListener, EntryAddedListener<Long,Message> {
        Consumer<Greeting[]> handler;
        MessageListener(Consumer<Greeting[]> handler) {
            this.handler = handler;
        }
        @Override
        public void entryAdded(EntryEvent<Long,Message> entryEvent) {
            Message newMessage = entryEvent.getValue();
            handler.accept(new Greeting[] {new Greeting(entryEvent.getKey(), newMessage.getMessage(), newMessage.getSender())});
        }
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        String user = event.getUser().getName();
        LOGGER.info("User {} disconnected", user);
        IMap<Long,Message> userMap = hazelcastInstance.getMap(Names.mapNameForUser(user));
        userMap.removeEntryListener(attachedUserMessages.remove(user));
        ;
    }
}
