package com.spring.boot.demo.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.demo.model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class EventsRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EventsRepository eventsRepository;

    private ObjectMapper mapper = new ObjectMapper();

    //private static File EVENT_CREATE = Paths.get("src","tst","resources","data","eventCreate.json").toFile();


    @BeforeEach
    void setup() throws IOException {
        Path resourceDirectory = Paths.get("src","test","resources");
        Path tstRes = resourceDirectory.resolve("data/eventCreate.json");
        String absolutePath = tstRes.toFile().getAbsolutePath();
        File file = new File(absolutePath);
        Event[] events = mapper.readValue(file,Event[].class);
        Arrays.stream(events).forEach(mongoTemplate::save);

    }

    @AfterEach
    void destroy(){
        //mongoTemplate.dropCollection("Events");
    }

    @Test
    @DisplayName("Repository - Create Event")
    public void createEvent(){
        //given
        Event testEvent = new Event("id", "testEvent", "testEventDescription", "Amsterdam", "John doe", new Date());


        // when
        Event savedEvent = eventsRepository.save(testEvent);

        //then

        then(savedEvent.getId())
                .as("Check if the Id generated for the saved Event")
                .isNotEmpty();
    }

    @Test
    @DisplayName("Repository - Find Event by Id")
    public void testFindEventById(){

        //given
        Event testEvent = new Event("id", "name", "description", "Amsterdam", "John doe", new Date());



        // when
        Optional<Event> fetchEvent = eventsRepository.findById("1");

        //then

        then(fetchEvent.get().getId())
                .as("Validate the Id as saved")
                .isEqualTo(testEvent.getId());
    }
}