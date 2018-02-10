package com.possemeeg.project2017.engine.model;

import com.possemeeg.project2017.shared.model.Message;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "broadcast")
public class BroadcastEntity {
    @Id
    @Column(name="message_id")
    private Long messageId;
    @JoinColumn(name = "message_id",referencedColumnName = "id")
    @OneToOne(optional=false, cascade= CascadeType.ALL)
    private MessageEntity messageEntity;

    public BroadcastEntity() {
    }

    public BroadcastEntity(MessageEntity messageEntity) {
        this.messageId = messageEntity.getId();
        this.messageEntity = messageEntity;
    }

    public static BroadcastEntity valueOf(MessageEntity message) {
        return new BroadcastEntity(message);
    }
    public static BroadcastEntity valueOf(Message message) {
        return valueOf(MessageEntity.valueOf(message));
    }
    public Message toMessage() {
        return messageEntity.toMessage();
    }
    public long getId() {
        return messageId;
    }
}

