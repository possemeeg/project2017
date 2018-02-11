package com.possemeeg.project2017.engine.haz;

import com.hazelcast.core.MapStore;
import com.possemeeg.project2017.engine.data.BroadcastRepository;
import com.possemeeg.project2017.engine.data.DirectiveIdGenerator;
import com.possemeeg.project2017.engine.data.DirectiveRepository;
import com.possemeeg.project2017.engine.model.BroadcastEntity;
import com.possemeeg.project2017.engine.model.DirectiveEntity;
import com.possemeeg.project2017.shared.model.Directive;
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
public class BroadcastStore implements MapStore<Long,Directive> {
    private final static Logger LOGGER = LoggerFactory.getLogger(BroadcastStore.class);
    private final BroadcastRepository broadcastRepository;
    private final DirectiveIdGenerator directiveIdGenerator;

    @Autowired 
    public BroadcastStore(BroadcastRepository broadcastRepository, DirectiveIdGenerator directiveIdGenerator) {
        this.broadcastRepository = broadcastRepository;
        this.directiveIdGenerator = directiveIdGenerator;
    }

    @Override
    public void store(Long aLong, Directive directive) {
        LOGGER.info("storing broadcast directive {}", aLong);
        broadcastRepository.save(BroadcastEntity.valueOf(directive));
    }

    @Override
    public void storeAll(Map<Long, Directive> map) {
        LOGGER.info("storing {} broadcast directives", map.size());
        broadcastRepository.saveAll(map.values().stream().map(BroadcastEntity::valueOf).collect(Collectors.toList()));
    }

    @Override
    public void delete(Long directiveId) {
        LOGGER.info("Deleting {}", directiveId);
        broadcastRepository.deleteById(directiveId);
    }

    @Override
    public void deleteAll(Collection<Long> collection) {
        LOGGER.info("Deleting {} directives", collection.size());
        for (Long directiveId : collection) {
            delete(directiveId);
        }
    }

    @Override
    public Directive load(Long directiveId) {
        LOGGER.info("Loading {}", directiveId);
        return broadcastRepository.findById(directiveId).map(BroadcastEntity::toDirective).orElse(null);
    }

    @Override
    public Map<Long, Directive> loadAll(Collection<Long> collection) {
        LOGGER.info("Loading {} directives", collection.size());
        return StreamSupport.stream(broadcastRepository.findAllById(collection).spliterator(), true)
            .collect(Collectors.toMap(BroadcastEntity::getId, BroadcastEntity::toDirective));
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        LOGGER.info("load all keys");
        Iterable<Long> keys = StreamSupport.stream(broadcastRepository.findAll().spliterator(), true)
            .map(BroadcastEntity::getId).collect(Collectors.toList());
        directiveIdGenerator.setAbove(keys);
        return keys;
    }
}

