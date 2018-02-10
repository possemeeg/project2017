package com.possemeeg.project2017.engine.data;

import com.possemeeg.project2017.engine.model.MessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<MessageEntity, Long> {

}

