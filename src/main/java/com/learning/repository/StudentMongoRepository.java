package com.learning.repository;


import com.learning.entity.StudentCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentMongoRepository extends MongoRepository<StudentCollection, Long> {

}


