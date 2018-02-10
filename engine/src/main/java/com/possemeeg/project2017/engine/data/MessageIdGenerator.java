package com.possemeeg.project2017.engine.data;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.possemeeg.project2017.shared.reference.Names;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class MessageIdGenerator {
    private final IAtomicLong nextId;

    @Autowired
    public MessageIdGenerator(HazelcastInstance hazelcastInstance) {
        this.nextId = hazelcastInstance.getAtomicLong(Names.MESSAGE_ID_GENERATOR);
    }

    public void setAbove(Iterable<Long> keys) {
        Iterator<Long> itr = keys.iterator();
        Long maxId = itr.hasNext() ? itr.next() : 0L;
        nextId.getAndAlter(id -> Long.max(id, maxId));
    }

}

