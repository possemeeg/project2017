package com.possemeeg.project2017.engine.model;

import com.possemeeg.project2017.shared.model.Directive;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "localuser_directive")
public class LocalUserDirectiveEntity {
    @Id
    @Column(name="id")
    private Long id;
    @JoinColumn(name = "id",referencedColumnName = "id")
    @OneToOne(optional=false, cascade= CascadeType.ALL)
    private DirectiveEntity directiveEntity;
    @Column(name="localuser_username")
    private String username;

    public LocalUserDirectiveEntity() {
    }

    public LocalUserDirectiveEntity(DirectiveEntity directiveEntity, String username) {
        this.id = directiveEntity.getId();
        this.directiveEntity = directiveEntity;
        this.username = username;
    }

    public static LocalUserDirectiveEntity valueOf(DirectiveEntity directiveEntity, String username) {
        return new LocalUserDirectiveEntity(directiveEntity, username);
    }
    public static LocalUserDirectiveEntity valueOf(Directive directive, String username) {
        return new LocalUserDirectiveEntity(DirectiveEntity.valueOf(directive), username);
    }

    public Directive toDirective() {
        return directiveEntity.toDirectiveForUser(username);
    }
    public long getId() {
        return id;
    }
}

