package com.spring.boot.demo.controller;

import com.spring.boot.demo.Service.EventService;
import com.spring.boot.demo.exception.bean.EventNotFoundException;
import com.spring.boot.demo.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("events")
@Slf4j
public class EventController {

    @Autowired
    EventService eventService;
    @PostMapping("/events")
    public ResponseEntity createEvent(@RequestBody @Valid Event event){
        Event createdEvent = eventService.postEvent(event);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEvent.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/eventsWithTemplate")
    public ResponseEntity<?> createEventWithTemplate(@RequestBody @NotNull @Valid Event event){
        Event createdEvent = eventService.postEventWithTemplate(event);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEvent.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/events/{id}")
    public  ResponseEntity<?> findEventById(@PathVariable @NotNull String id){
        return eventService.findEventById(id)
                .map(event -> {
                    return ResponseEntity
                            .ok()
                            .body(event);
                }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEventById(@PathVariable @NotNull String id, @RequestBody @NotNull Event event){
        return eventService.updateEventById(id, event)
                .map(e -> {
                    return ResponseEntity
                            .ok()
                            .body(e);
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/events")
    public ResponseEntity<?> findEvents(@RequestParam(name = "venue" ) Optional<String> venue) throws EventNotFoundException {

        List<Event> eventsFound = venue.isPresent()?
                eventService.getEventsByVenue(venue.get())
                : eventService.findEvents();
        if(eventsFound.size()==0) throw new EventNotFoundException("No Event found");
        return   ResponseEntity
                            .ok()
                            .body(eventsFound);
    }
}
