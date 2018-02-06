package com.possemeeg.project2017.webdoor.controller;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.MapListener;
import com.hazelcast.query.extractor.ValueCollector;
import com.hazelcast.query.extractor.ValueExtractor;
import com.possemeeg.project2017.webdoor.model.client.UserChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.session.MapSession;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class UserListController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserListController.class);

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final IMap<String,MapSession> users;
    private final ValueExtractor<MapSession, String> principalNameExtractor;
    private final Updater online;
    private final Updater offline;

    @Autowired
    public UserListController(SimpMessagingTemplate simpMessagingTemplate, HazelcastInstance hazelcastInstance) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.users = hazelcastInstance.getMap(HazelcastSessionRepository.DEFAULT_SESSION_MAP_NAME);
        this.principalNameExtractor = new PrincipalNameExtractor();
        this.users.addEntryListener(new UserEntryListener(), true);
        this.online = new Updater(true);
        this.offline = new Updater(false);
    }

    @SubscribeMapping("/general.users")
    public Collection<UserChange> userSubscribed(Principal principal) {
        Collector<MapSession, ChangeCollector, List<UserChange>>  col =
            Collector.of(
                    () -> new ChangeCollector(),
                    (c,item) -> principalNameExtractor.extract(item, null, c),
                    (c1,c2) -> c1.merge(c2),
                    ChangeCollector::finish);
        return users.values().stream().collect(col);
    }

    private class UserEntryListener implements MapListener, EntryAddedListener<String, MapSession>, EntryRemovedListener<String, MapSession> {
        @Override
        public void entryAdded(EntryEvent<String, MapSession> entryEvent) {
            principalNameExtractor.extract(entryEvent.getValue(), null, online);
        }
        @Override
        public void entryRemoved(EntryEvent<String, MapSession> entryEvent) {
            principalNameExtractor.extract(entryEvent.getOldValue(), null, offline);
        }
    }

    private class ChangeCollector extends ValueCollector<String> {
        final List<UserChange> list = new ArrayList<UserChange>();
        @Override
        public void addObject(String principalName) {
            list.add(new UserChange(principalName, true));
        }
        
        ChangeCollector merge(ChangeCollector other) {
            list.addAll(other.list);
            return this;
        }
        List<UserChange> finish() {
            return list;
        }
    }

    private class Updater extends ValueCollector<String>  {
        final boolean online;

        Updater(boolean online) {
            this.online = online;
        }

        @Override
        public void addObject(String principalName) {
            LOGGER.info("New item on {}: {}", HazelcastSessionRepository.DEFAULT_SESSION_MAP_NAME, principalName);
            simpMessagingTemplate.convertAndSend("/app/general.users", new UserChange[] {new UserChange(principalName, online)});
        }
    }
}

