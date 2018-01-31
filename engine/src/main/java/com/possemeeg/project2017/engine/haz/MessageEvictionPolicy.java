package com.possemeeg.project2017.engine.haz;

import com.hazelcast.core.EntryView;
import com.hazelcast.map.eviction.MapEvictionPolicy;
import com.possemeeg.project2017.shared.model.MessageKey;

public class MessageEvictionPolicy extends MapEvictionPolicy {
    @Override
    public int compare(EntryView entryView1, EntryView entryView2) {
        return MessageKey.compare((MessageKey)entryView2.getKey(), (MessageKey)entryView1.getKey());
    }
}

