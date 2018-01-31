package com.possemeeg.project2017.webdoor.controller;

import com.google.common.collect.ImmutableList;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.possemeeg.project2017.shared.model.Message;
import com.possemeeg.project2017.webdoor.component.MessageExchange;
import com.possemeeg.project2017.webdoor.model.MessageToUser;
import com.possemeeg.project2017.webdoor.model.MessageFromUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class MessagingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingController.class);
    public final MessageExchange messageExchange;

    @Autowired
    public MessagingController(MessageExchange messageExchange) {
        this.messageExchange = messageExchange;
    }

    @MessageMapping("/send-message")
    public void greeting(MessageFromUser message, Principal user) throws Exception {
        if (message.isForAll()) {
            messageExchange.broadcastMessage(user.getName(), message.getMessage());
        } else {
            messageExchange.postMessageToUsers(user.getName(), message.getMessage(), ImmutableList.of(message.getRecipient()));
        }
    }

    //@SubscribeMapping("/topic/personal-messages")
    //public Collection<MessageToUser> userSubscribedPersonal(Principal principal) {
    //  String user = principal.getName();
    //  return messages.stream().filter(message -> message.isForUser(user)).map(MessageToUser::fromMessage).collect(Collectors.toList());
    //}
    @SubscribeMapping("/topic/global-messages")
    public Collection<MessageToUser> userSubscribedGlobal(Principal principal) {
        return messageExchange.getUserMessages(principal.getName());
    }
}
