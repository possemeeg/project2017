package com.possemeeg.project2017.engine.model;

import com.possemeeg.project2017.shared.model.Directive;

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
    @Column(name="id")
    private Long id;
    @JoinColumn(name = "id",referencedColumnName = "id")
    @OneToOne(optional=false, cascade= CascadeType.ALL)
    private DirectiveEntity directiveEntity;

    public BroadcastEntity() {
    }

    public BroadcastEntity(DirectiveEntity directiveEntity) {
        this.id = directiveEntity.getId();
        this.directiveEntity = directiveEntity;
    }

    public static BroadcastEntity valueOf(DirectiveEntity directive) {
        return new BroadcastEntity(directive);
    }
    public static BroadcastEntity valueOf(Directive directive) {
        return valueOf(DirectiveEntity.valueOf(directive));
    }
    public Directive toDirective() {
        return directiveEntity.toDirective();
    }
    public long getId() {
        return id;
    }
}

