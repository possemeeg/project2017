package com.possemeeg.project2017.engine.haz;

import com.hazelcast.core.MapStore;
import com.possemeeg.project2017.engine.data.LocalUserDirectiveRepository;
import com.possemeeg.project2017.engine.data.DirectiveIdGenerator;
import com.possemeeg.project2017.engine.model.LocalUserDirectiveEntity;
import com.possemeeg.project2017.shared.model.Directive;
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
public class DirectiveStore implements MapStore<Long,Directive> {
    private final static Logger LOGGER = LoggerFactory.getLogger(DirectiveStore.class);
    private final LocalUserDirectiveRepository localUserDirectiveRepository;
    private String username;
    private final DirectiveIdGenerator directiveIdGenerator;

    @Autowired 
    public DirectiveStore(LocalUserDirectiveRepository localUserDirectiveRepository,
            DirectiveIdGenerator directiveIdGenerator) {
        this.localUserDirectiveRepository = localUserDirectiveRepository;
        this.directiveIdGenerator = directiveIdGenerator;
    }

    public void setUser(String username) {
        this.username = username;
        LOGGER.info("Directive store created for username {}", username);
    }

    @Override
    public void store(Long directiveId, Directive directive) {
        LOGGER.info("storing directive {}", directiveId);
        localUserDirectiveRepository.save(LocalUserDirectiveEntity.valueOf(directive, username));
    }

    @Override
    public void storeAll(Map<Long, Directive> map) {
        LOGGER.info("storing {} directives", map.size());
        localUserDirectiveRepository.saveAll(map.values().stream()
                .map(msg -> LocalUserDirectiveEntity.valueOf(msg, username))
                .collect(Collectors.toList()));
    }

    @Override
    public void delete(Long directiveId) {
        LOGGER.info("Deleting directive {}", directiveId);
        localUserDirectiveRepository.deleteById(directiveId);
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
        LOGGER.info("Loading directive {}", directiveId);
        return localUserDirectiveRepository.findById(directiveId).map(LocalUserDirectiveEntity::toDirective).orElse(null);
    }

    @Override
    public Map<Long, Directive> loadAll(Collection<Long> ids) {
        LOGGER.info("Loading {} directives", ids.size());
        return StreamSupport.stream(localUserDirectiveRepository.findAllById(ids).spliterator(), true)
            .collect(Collectors.toMap(LocalUserDirectiveEntity::getId, LocalUserDirectiveEntity::toDirective));
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        LOGGER.info("load all directives keys for username {}", username);
        Iterable<Long> keys = StreamSupport.stream(localUserDirectiveRepository.findByUsername(username).spliterator(), true)
            .map(LocalUserDirectiveEntity::getId).sorted(Collections.reverseOrder()).collect(Collectors.toList());
        directiveIdGenerator.setAbove(keys);
        return keys;
    }
}

