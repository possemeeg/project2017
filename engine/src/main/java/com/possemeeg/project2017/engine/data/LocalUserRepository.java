package com.possemeeg.project2017.engine.data;

import com.possemeeg.project2017.engine.model.LocalUserEntity;
import org.springframework.data.repository.CrudRepository;

public interface LocalUserRepository extends CrudRepository<LocalUserEntity, String> {

}

