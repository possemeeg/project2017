package com.possemeeg.project2017.engine.data;

import com.possemeeg.project2017.engine.model.LocalUserDirectiveEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface LocalUserDirectiveRepository extends CrudRepository<LocalUserDirectiveEntity,Long> {

    List<LocalUserDirectiveEntity> findByUsername(String username);
}


