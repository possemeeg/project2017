package com.possemeeg.project2017.engine.haz;

import com.hazelcast.core.MapStore;
import com.possemeeg.project2017.engine.data.BroadcastRepository;
import com.possemeeg.project2017.engine.data.MessageIdGenerator;
import com.possemeeg.project2017.engine.data.MessageRepository;
import com.possemeeg.project2017.engine.model.BroadcastEntity;
import com.possemeeg.project2017.engine.model.MessageEntity;
import com.possemeeg.project2017.shared.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BroadcastStore implements MapStore<Long,Message> {
    private final static Logger LOGGER = LoggerFactory.getLogger(BroadcastStore.class);
    private final BroadcastRepository broadcastRepository;
    private final MessageIdGenerator messageIdGenerator;

    @Autowired 
    public BroadcastStore(BroadcastRepository broadcastRepository, MessageIdGenerator messageIdGenerator) {
        this.broadcastRepository = broadcastRepository;
        this.messageIdGenerator = messageIdGenerator;
    }

    @Override
    public void store(Long aLong, Message message) {
        LOGGER.info("storing broadcast message {}", aLong);
        broadcastRepository.save(BroadcastEntity.valueOf(message));
    }

    @Override
    public void storeAll(Map<Long, Message> map) {
        LOGGER.info("storing {} broadcast messages", map.size());
        broadcastRepository.saveAll(map.values().stream().map(BroadcastEntity::valueOf).collect(Collectors.toList()));
    }

    @Override
    public void delete(Long messageId) {
        LOGGER.info("Deleting {}", messageId);
        broadcastRepository.deleteById(messageId);
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
        LOGGER.info("Loading {}", messageId);
        return broadcastRepository.findById(messageId).map(BroadcastEntity::toMessage).orElse(null);
    }

    @Override
    public Map<Long, Message> loadAll(Collection<Long> collection) {
        LOGGER.info("Loading {} messages", collection.size());
        return StreamSupport.stream(broadcastRepository.findAllById(collection).spliterator(), true)
            .collect(Collectors.toMap(BroadcastEntity::getId, BroadcastEntity::toMessage));
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        LOGGER.info("load all keys");
        Iterable<Long> keys = StreamSupport.stream(broadcastRepository.findAll().spliterator(), true)
            .map(BroadcastEntity::getId).collect(Collectors.toList());
        messageIdGenerator.setAbove(keys);
        return keys;
    }
}

