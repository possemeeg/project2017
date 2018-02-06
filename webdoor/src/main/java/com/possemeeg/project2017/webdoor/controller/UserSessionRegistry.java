package com.possemeeg.project2017.webdoor.controller;

import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Component;

@Component
public class UserSessionRegistry extends SessionRegistryImpl {
    public void registerNewSession(String sessionId, Object principal) {
        super.registerNewSession(sessionId, principal);
    }
    public void removeSessionInformation(String sessionId) {
        super.removeSessionInformation(sessionId);
    }
}

