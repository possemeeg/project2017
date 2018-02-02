package com.possemeeg.project2017.webdoor.component;

import com.hazelcast.core.*;
import com.possemeeg.project2017.shared.model.Message;
import com.possemeeg.project2017.shared.reference.MessageMapNames;
import com.possemeeg.project2017.webdoor.model.MessageToUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class MessageRouter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);
    private final ProcessContext processContext;
    private final ISet<LoggedOnUser> loggedOnUsers;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageRouter(ProcessContext processContext, HazelcastInstance hazelcastInstance, SimpMessagingTemplate simpMessagingTemplate) {
        this.processContext = processContext;
        loggedOnUsers = hazelcastInstance.getSet(MessageMapNames.LOGGED_ON_USERS);
        loggedOnUsers.addItemListener(new ItemListener<String>() {
            @Override
            public void itemAdded(ItemEvent<Message> var1) {
            }
            @Override
            public void itemRemoved(ItemEvent<Message> var1) {
            }
        }, true);
        //messages = hazelcastInstance.getList(Names.MESSAGES);
        //this.simpMessagingTemplate = simpMessagingTemplate;

        //messages.addItemListener(new ItemListener<Message>() {
        //    @Override
        //    public void itemAdded(ItemEvent<Message> var1) {
        //        LOGGER.info("Item added to {}: {}", Names.MESSAGES, var1.getItem());
        //        Message newMessage = var1.getItem();
        //        if (newMessage.isForAll()) {
        //            simpMessagingTemplate.convertAndSend("/app/topic/global-messages",
        //                    new MessageToUser[] {new MessageToUser("id", newMessage.getMessage(), newMessage.getSender())});
        //        } else {
        //            for (String user : newMessage.getRecipients()) {
        //                simpMessagingTemplate.convertAndSendToUser(user, "/topic/personal-messages",
        //                        new MessageToUser("id", newMessage.getMessage(), newMessage.getSender()));
        //            }
        //        }
        //    }

        //    @Override
        //    public void itemRemoved(ItemEvent<Message> var1) {
        //        LOGGER.info("Item removed from {}: {}", Names.MESSAGES, var1.getItem());
        //    }
        //}, true);
    }

    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        LOGGER.info("User connected");
        loggedOnUsers.add(new LoggedOnUser(event.getUser().getName(), processContext));
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        LOGGER.info("User disconnected");
        loggedOnUsers.remove(new LoggedOnUser(event.getUser().getName(), processContext));
    }
}
