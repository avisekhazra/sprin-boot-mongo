package com.spring.boot.demo.repositories;

import com.spring.boot.demo.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends MongoRepository<Event,String> {
}
