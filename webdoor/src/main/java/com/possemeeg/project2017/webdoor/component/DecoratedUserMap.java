package com.possemeeg.project2017.webdoor.component;

import com.google.common.collect.ImmutableList;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.extractor.ValueCollector;
import com.hazelcast.query.extractor.ValueExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.MapSession;
import org.springframework.session.hazelcast.HazelcastSessionRepository;
import org.springframework.session.hazelcast.PrincipalNameExtractor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collector;

@Component
public class DecoratedUserMap {
    private final IMap<String,MapSession> users;
    private final ValueExtractor<MapSession, String> principalNameExtractor;

    @Autowired
    public DecoratedUserMap(HazelcastInstance hazelcastInstance) {
        this.users = hazelcastInstance.getMap(HazelcastSessionRepository.DEFAULT_SESSION_MAP_NAME);
        this.principalNameExtractor = new PrincipalNameExtractor();
    }

    public Collection<String> loggedOnUsers() {
        return loggedOnUsers(Function.identity());
    }
    public <E> Collection<E> loggedOnUsers(Function<String,E> converter) {
        Collector<MapSession, ChangeCollector<E>, Collection<E>> col =
            Collector.of(
                    () -> new ChangeCollector<E>(converter),
                    (c,item) -> principalNameExtractor.extract(item, null, c),
                    (c1,c2) -> c1.merge(c2),
                    ChangeCollector<E>::finish);
        return users.values().stream().collect(col);
    }

    private class ChangeCollector<E> extends ValueCollector<String> {
        final Function<String,E> converter;
        final ImmutableList.Builder<E> listBuilder = ImmutableList.builder();

        ChangeCollector(Function<String,E> converter) {
            this.converter = converter;
        }

        @Override
        public void addObject(String principalName) {
            listBuilder.add(converter.apply(principalName));
        }
        
        ChangeCollector<E> merge(ChangeCollector<E> other) {
            listBuilder.addAll(other.listBuilder.build());
            return this;
        }
        Collection<E> finish() {
            return listBuilder.build();
        }
    }

}

