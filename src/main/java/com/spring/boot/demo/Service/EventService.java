package com.spring.boot.demo.Service;
import com.spring.boot.demo.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

     Event getEventById(String id);

     Event postEvent(Event event);

     Event postEventWithTemplate(Event event);

     Optional<Event> updateEventById(String id , Event event);

     List<Event> getEventsByVenue(String venue);

     Optional<Event> findEventById(String id);

     List<Event> findEvents();
}
