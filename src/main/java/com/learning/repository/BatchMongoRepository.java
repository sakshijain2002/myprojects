package com.learning.repository;

import com.learning.entity.BatchCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BatchMongoRepository extends MongoRepository<BatchCollection,Long> {
}
