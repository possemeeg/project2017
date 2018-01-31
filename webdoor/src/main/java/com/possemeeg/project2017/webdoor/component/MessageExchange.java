package com.possemeeg.project2017.webdoor.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.possemeeg.project2017.shared.model.Message;
import com.possemeeg.project2017.shared.model.MessageKey;
import com.possemeeg.project2017.shared.reference.MessageMapNames;
import com.possemeeg.project2017.webdoor.model.MessageToUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MessageExchange {
    private final HazelcastInstance hazelcastInstance;
    private final IMap<MessageKey, Message> globalMessageMap;

    @Autowired
    public MessageExchange(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.globalMessageMap = hazelcastInstance.getMap(MessageMapNames.MESSAGES_GLOBAL_NAME);
    }

    public void postMessageToUsers(String sender, String messageText, List<String> recipients) {
        Message message = new Message(sender, messageText);
        for (String recipient : recipients) {
            String mapName = MessageMapNames.userMapName(recipient);
            IMap<MessageKey,Message> map = hazelcastInstance.getMap(mapName);
            map.put(MessageKey.create(hazelcastInstance.getIdGenerator(mapName).newId()), message);
        }
    }
    public void broadcastMessage(String sender, String message) {
        globalMessageMap
            .put(MessageKey.create(hazelcastInstance.getIdGenerator(MessageMapNames.MESSAGES_GLOBAL_NAME).newId()),
                    new Message(sender, message));
    }

    public Collection<MessageToUser> getUserMessages(String user) {
        IMap<MessageKey,Message> userMap = hazelcastInstance.getMap(MessageMapNames.userMapName(user));
        return Stream.concat(globalMessageMap.entrySet().stream(), userMap.entrySet().stream())
            .sorted((entry1, entry2) -> MessageKey.compare(entry1.getKey(), entry2.getKey()))
            .map(msg -> MessageToUser.fromMessage(msg.getValue()))
            .collect(Collectors.toList());
    }
}

