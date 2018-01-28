package com.possemeeg.project2017.webdoor.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
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
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class MessageRouter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);
    private final IList<Message> messages;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageRouter(HazelcastInstance hazelcastInstance, SimpMessagingTemplate simpMessagingTemplate) {
        messages = hazelcastInstance.getList(Names.MESSAGES);
        this.simpMessagingTemplate = simpMessagingTemplate;

        messages.addItemListener(new ItemListener<Message>() {
            @Override
            public void itemAdded(ItemEvent<Message> var1) {
                LOGGER.info("Item added to {}: {}", Names.MESSAGES, var1.getItem());
                Message newMessage = var1.getItem();
                if (newMessage.isForAll()) {
                    simpMessagingTemplate.convertAndSend("/app/general.greetings",
                            new Greeting[] {new Greeting("id", newMessage.getMessage(), newMessage.getSender())});
                } else {
                    for (String user : newMessage.getRecipients()) {
                        simpMessagingTemplate.convertAndSendToUser(user, "/topic/personal-greetings",
                                new Greeting("id", newMessage.getMessage(), newMessage.getSender()));
                    }
                }
            }

            @Override
            public void itemRemoved(ItemEvent<Message> var1) {
                LOGGER.info("Item removed from {}: {}", Names.MESSAGES, var1.getItem());
            }
        }, true);
    }

    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        LOGGER.info("User connected");
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        LOGGER.info("User disconnected");
    }
}
