package com.learning.repository;

import com.learning.entity.TimeSlotCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  TimeSlotMongoRepo  extends MongoRepository<TimeSlotCollection,Long> {
}
