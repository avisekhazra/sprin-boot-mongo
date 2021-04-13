package com.spring.boot.demo.Service.impl;


import com.spring.boot.demo.Service.EventService;
import com.spring.boot.demo.model.Event;
import com.spring.boot.demo.repositories.EventsRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Event getEventById(String id) {
        return null;
    }

    @Override
    public Event postEvent(Event event) {
        return eventsRepository.save(event);
    }

    @Override
    public Event postEventWithTemplate(Event event) {
        return mongoTemplate.insert(event);
    }

    @Override
    public Optional<Event> updateEventById(String id , Event event) {

        var query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
        var update = new Update();
        update.set("name",event.getName());
        update.set("description",event.getDescription());
        mongoTemplate.updateFirst(query, update, Event.class);
        return eventsRepository.findById(id);

    }

    @Override
    public List<Event> getEventsByVenue(String venue) {
        var query = new Query();
        query.addCriteria(Criteria.where("venue").is(venue));
        return mongoTemplate.find(query,Event.class);

    }

    @Override
    public Optional<Event> findEventById(String id) {
        return eventsRepository.findById(id);
    }

    @Override
    public List<Event> findEvents() {
        return mongoTemplate.findAll(Event.class);
    }
}
