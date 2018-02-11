package com.possemeeg.project2017.engine.model;

import com.possemeeg.project2017.shared.model.Directive;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "directive")
public class DirectiveEntity {
    @Id
    private long id;
    private String textContent;
    private String sender;

    public DirectiveEntity() {
    }

    private DirectiveEntity(long id, String textContent, String sender) {
        this.id = id;
        this.textContent = textContent;
        this.sender = sender;
    }

    public static DirectiveEntity valueOf(Directive directive) {
        return new DirectiveEntity(directive.getId(), directive.getTextContent(), directive.getSender());
    }

    public Directive toDirective() {
        return Directive.forAll(id, textContent, sender);
    }

    public Directive toDirectiveForUser(String user) {
        return Directive.forUser(id, textContent, sender, user);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

