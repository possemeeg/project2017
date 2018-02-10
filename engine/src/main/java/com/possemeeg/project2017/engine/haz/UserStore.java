package com.possemeeg.project2017.engine.haz;

import com.hazelcast.core.MapStore;
import com.possemeeg.project2017.engine.data.LocalUserRepository;
import com.possemeeg.project2017.engine.model.LocalUserEntity;
import com.possemeeg.project2017.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserStore implements MapStore<String,User> {
    private static Logger LOGGER = LoggerFactory.getLogger(UserStore.class);

    private final LocalUserRepository localUserRepository;

    @Autowired
    public UserStore(LocalUserRepository localUserRepository) {
        this.localUserRepository = localUserRepository;
    }

    @Override
    public void store(String key, User user) {
        LOGGER.info("storing user {}", key);
        localUserRepository.save(LocalUserEntity.valueOf(user));
    }

    @Override
    public void storeAll(Map<String, User> map) {

        LOGGER.info("storing {} users", map.size());
        localUserRepository.saveAll(map.values().stream().map(LocalUserEntity::valueOf).collect(Collectors.toList()));
    }

    @Override
    public void delete(String username) {
        LOGGER.info("Deleting user {}", username);
        localUserRepository.deleteById(username);
    }

    @Override
    public void deleteAll(Collection<String> usernames) {
        LOGGER.info("Deleting {} users", usernames.size());
        for (String username : usernames) {
            delete(username);
        }
    }

    @Override
    public User load(String username) {
        LOGGER.info("Loading user {}", username);
        return localUserRepository.findById(username).map(LocalUserEntity::toUser).orElse(null);
    }

    @Override
    public Map<String, User> loadAll(Collection<String> usernames) {
        LOGGER.info("Loading {} messages", usernames.size());
        return StreamSupport.stream(localUserRepository.findAllById(usernames).spliterator(), true)
            .collect(Collectors.toMap(LocalUserEntity::getUsername, LocalUserEntity::toUser));
    }

    @Override
    public Iterable<String> loadAllKeys() {
        LOGGER.info("load all keys");
        return StreamSupport.stream(localUserRepository.findAll().spliterator(), true)
            .map(LocalUserEntity::getUsername).collect(Collectors.toList());
    }
}

