package com.possemeeg.project2017.engine.model;

import com.possemeeg.project2017.shared.model.Message;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "localuser_message")
public class LocalUserMessageEntity {
    @Id
    @Column(name="message_id")
    private Long messageId;
    @JoinColumn(name = "message_id",referencedColumnName = "id")
    @OneToOne(optional=false, cascade= CascadeType.ALL)
    private MessageEntity messageEntity;
    @Column(name="localuser_username")
    private String username;

    public LocalUserMessageEntity() {
    }

    public LocalUserMessageEntity(MessageEntity messageEntity, String username) {
        this.messageId = messageEntity.getId();
        this.messageEntity = messageEntity;
        this.username = username;
    }

    public static LocalUserMessageEntity valueOf(MessageEntity messageEntity, String username) {
        return new LocalUserMessageEntity(messageEntity, username);
    }
    public static LocalUserMessageEntity valueOf(Message message, String username) {
        return new LocalUserMessageEntity(MessageEntity.valueOf(message), username);
    }

    public Message toMessage() {
        return messageEntity.toMessageForUser(username);
    }
    public long getId() {
        return messageId;
    }
}

