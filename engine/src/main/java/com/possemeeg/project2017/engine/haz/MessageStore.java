package com.possemeeg.project2017.engine.haz;

import com.hazelcast.core.MapStore;
import com.possemeeg.project2017.shared.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@Scope("prototype")
public class MessageStore implements MapStore<Long,Message> {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageStore.class);
    public MessageStore(String username) {
        LOGGER.info("Message store created");
    }

    @Override
    public void store(Long aLong, Message message) {
        LOGGER.info("Message {} stored", aLong);
    }

    @Override
    public void storeAll(Map<Long, Message> map) {
        for (Map.Entry<Long, Message> entry : map.entrySet()) {
            store(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void delete(Long aLong) {
        LOGGER.info("Message {} deleted", aLong);
    }

    @Override
    public void deleteAll(Collection<Long> collection) {
        for (Long entry : collection) {
            delete(entry);
        }
    }

    @Override
    public Message load(Long aLong) {
        LOGGER.info("Message {} loaded", aLong);
        return null;
    }

    @Override
    public Map<Long, Message> loadAll(Collection<Long> collection) {
        for (Long entry : collection) {
            load(entry);
        }
        return null;
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        return null;
    }
}

