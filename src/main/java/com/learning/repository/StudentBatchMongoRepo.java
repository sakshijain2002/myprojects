package com.learning.repository;

import com.learning.entity.StudentBatchCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentBatchMongoRepo extends MongoRepository<StudentBatchCollection,Long> {
}
