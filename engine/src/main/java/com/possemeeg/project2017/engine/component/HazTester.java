package com.possemeeg.project2017.engine.component;

import com.hazelcast.core.HazelcastInstance;
import com.possemeeg.project2017.shared.model.Message;
import com.possemeeg.project2017.shared.reference.Names;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class HazTester {
    public HazTester(HazelcastInstance haz) {
        //ScheduledExecutorService sch = Executors.newScheduledThreadPool(10);
        //sch.schedule(() -> {
        //    haz.getMap(Names.BROADCAST_MESSAGE_MAP).get(0L);
        //}, 3, TimeUnit.SECONDS);
        //sch.schedule(() -> {
        //    haz.getMap(Names.USER_MESSAGE_MAP_PREFIX + "dev").get(0L);
        //}, 4, TimeUnit.SECONDS);
        //sch.schedule(() -> {
        //    haz.getMap(Names.BROADCAST_MESSAGE_MAP).put(0L, Message.forAll(0L, "test", "dev"));
        //}, 5, TimeUnit.SECONDS);
        //sch.schedule(() -> {
        //    haz.getMap(Names.BROADCAST_MESSAGE_MAP).get(0L);
        //}, 6, TimeUnit.SECONDS);
    }

}

