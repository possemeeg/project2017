package com.possemeeg.project2017.engine.haz;

import com.hazelcast.core.MapStore;
import com.possemeeg.project2017.engine.data.LocalUserMessageRepository;
import com.possemeeg.project2017.engine.data.MessageIdGenerator;
import com.possemeeg.project2017.engine.model.LocalUserMessageEntity;
import com.possemeeg.project2017.shared.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Scope("prototype")
public class MessageStore implements MapStore<Long,Message> {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageStore.class);
    private final LocalUserMessageRepository localUserMessageRepository;
    private String username;
    private final MessageIdGenerator messageIdGenerator;

    @Autowired 
    public MessageStore(LocalUserMessageRepository localUserMessageRepository,
            MessageIdGenerator messageIdGenerator) {
        this.localUserMessageRepository = localUserMessageRepository;
        this.messageIdGenerator = messageIdGenerator;
    }

    public void setUser(String username) {
        this.username = username;
        LOGGER.info("Message store created for username {}", username);
    }

    @Override
    public void store(Long aLong, Message message) {
        LOGGER.info("storing message {}", aLong);
        localUserMessageRepository.save(LocalUserMessageEntity.valueOf(message, username));
    }

    @Override
    public void storeAll(Map<Long, Message> map) {
        LOGGER.info("storing {} messages", map.size());
        localUserMessageRepository.saveAll(map.values().stream()
                .map(msg -> LocalUserMessageEntity.valueOf(msg, username))
                .collect(Collectors.toList()));
    }

    @Override
    public void delete(Long messageId) {
        LOGGER.info("Deleting message {}", messageId);
        localUserMessageRepository.deleteById(messageId);
    }

    @Override
    public void deleteAll(Collection<Long> collection) {
        LOGGER.info("Deleting {} messages", collection.size());
        for (Long messageId : collection) {
            delete(messageId);
        }
    }

    @Override
    public Message load(Long messageId) {
        LOGGER.info("Loading message {}", messageId);
        return localUserMessageRepository.findById(messageId).map(LocalUserMessageEntity::toMessage).orElse(null);
    }

    @Override
    public Map<Long, Message> loadAll(Collection<Long> ids) {
        LOGGER.info("Loading {} messages", ids.size());
        return StreamSupport.stream(localUserMessageRepository.findAllById(ids).spliterator(), true)
            .collect(Collectors.toMap(LocalUserMessageEntity::getId, LocalUserMessageEntity::toMessage));
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        LOGGER.info("load all messages keys for username {}", username);
        Iterable<Long> keys = StreamSupport.stream(localUserMessageRepository.findByUsername(username).spliterator(), true)
            .map(LocalUserMessageEntity::getId).sorted(Collections.reverseOrder()).collect(Collectors.toList());
        messageIdGenerator.setAbove(keys);
        return keys;
    }
}

