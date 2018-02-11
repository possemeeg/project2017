package com.possemeeg.project2017.webdoor.model;

import com.possemeeg.project2017.shared.model.Directive;
import com.google.common.base.Strings;

public class ClientDirective {

    private long id;
    private String textContent;
    private String sender;
    private String recipient;

    public ClientDirective() {
    }

    public ClientDirective(long id, String textContent, String sender, String recipient) {
        this.id = id;
        this.textContent = textContent;
        this.sender = sender;
        this.recipient = recipient;
    }

    public static ClientDirective fromDirective(Directive directive) {
        return new ClientDirective(directive.getId(), directive.getTextContent(), directive.getSender(), null);
    }

    public long getId() {
        return id;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public boolean isForAll() {
      return Strings.isNullOrEmpty(recipient);
    }
}
