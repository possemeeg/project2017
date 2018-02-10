package com.possemeeg.project2017.engine.data;

import com.possemeeg.project2017.engine.model.LocalUserMessageEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface LocalUserMessageRepository extends CrudRepository<LocalUserMessageEntity,Long> {

    List<LocalUserMessageEntity> findByUsername(String username);
}


