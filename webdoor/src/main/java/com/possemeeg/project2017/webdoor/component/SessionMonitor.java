package com.possemeeg.project2017.webdoor.component;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

@Component
public class SessionMonitor implements HttpSessionBindingListener {
    @Override
    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {

    }
}

