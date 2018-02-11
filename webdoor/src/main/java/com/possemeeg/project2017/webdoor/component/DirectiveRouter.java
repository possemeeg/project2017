package com.possemeeg.project2017.webdoor.component;

import com.hazelcast.core.*;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.MapListener;
import com.possemeeg.project2017.shared.model.Directive;
import com.possemeeg.project2017.shared.reference.Names;
import com.possemeeg.project2017.webdoor.model.ClientDirective;
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
public class DirectiveRouter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectiveRouter.class);
    private final HazelcastInstance hazelcastInstance;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Map<String,String> attachedUserDirectives = new ConcurrentHashMap<>();
    private final IMap<Long,Directive> broadcasts;

    @Autowired
    public DirectiveRouter(HazelcastInstance hazelcastInstance, SimpMessagingTemplate simpMessagingTemplate) {
        this.hazelcastInstance = hazelcastInstance;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.broadcasts = hazelcastInstance.getMap(Names.BROADCAST_MESSAGE_MAP);

        Consumer<ClientDirective[]> broadcastHandler =
            msgs ->
               attachedUserDirectives.keySet().stream().forEach(user ->
                       simpMessagingTemplate.convertAndSendToUser(user, "/personal.directives", msgs));
        broadcasts.addEntryListener(new DirectiveListener(broadcastHandler), true);
    }

    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        String user = event.getUser().getName(); 
        LOGGER.info("User {} connected", user);
        IMap<Long,Directive> userMap = hazelcastInstance.getMap(Names.mapNameForUser(user));
        DirectiveListener userDirectiveListener = new DirectiveListener(msgs ->
                simpMessagingTemplate.convertAndSendToUser(user, "/personal.directives", msgs));
        attachedUserDirectives.put(user, userMap.addEntryListener(userDirectiveListener, true));
    }

    private class DirectiveListener implements MapListener, EntryAddedListener<Long,Directive> {
        Consumer<ClientDirective[]> handler;
        DirectiveListener(Consumer<ClientDirective[]> handler) {
            this.handler = handler;
        }
        @Override
        public void entryAdded(EntryEvent<Long,Directive> entryEvent) {
            Directive newDirective = entryEvent.getValue();
            handler.accept(new ClientDirective[] {ClientDirective.fromDirective(newDirective)});
        }
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        String user = event.getUser().getName();
        LOGGER.info("User {} disconnected", user);
        IMap<Long,Directive> userMap = hazelcastInstance.getMap(Names.mapNameForUser(user));
        userMap.removeEntryListener(attachedUserDirectives.remove(user));
        ;
    }
}
