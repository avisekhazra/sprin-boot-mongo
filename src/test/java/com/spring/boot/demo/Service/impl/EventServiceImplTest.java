package com.spring.boot.demo.Service.impl;

import com.mongodb.client.MongoClients;
import com.spring.boot.demo.Service.EventService;
import com.spring.boot.demo.configuration.MongoConfigurations;
import com.spring.boot.demo.model.Event;
import com.spring.boot.demo.repositories.EventsRepository;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.SocketUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class EventServiceImplTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private EventsRepository eventsRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    private static final String CONNECTION_STRING = "mongodb://%s:%d";

    private MongodExecutable mongodExecutable;


    /* @BeforeEach
    void setup() throws Exception {
        String ip = "localhost";
        int randomPort = SocketUtils.findAvailableTcpPort();

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(ip, randomPort, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, ip, randomPort)),"test");
    }**/

    @Test
    @DisplayName("Post event Service Method Success")
    public void testPostEventSuccess(){
        //given

        Event testEvent = new Event("id", "testEvent", "testEventDescription", "Amsterdam", "John doe",new Date());
        given(eventsRepository.save(any())).willReturn(testEvent);

        //when
        Event savedEvent = eventService.postEvent(testEvent);

        //then

        then(savedEvent.getName())
                .as("Check the name for the input event with the saved event")
                .isEqualTo(testEvent.getName());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedEvent.getName())
                    .as("Check the name for the input event with the saved event")
                    .isEqualTo(testEvent.getName());
            softly.assertThat(savedEvent.getDescription())
                    .as("Check the description for the input event with the saved event")
                    .isEqualTo(testEvent.getDescription());
            softly.assertThat(savedEvent.getId())
                    .as("Check the Id exists")
                    .isNotEmpty();
        });


    }
    @Test
    @DisplayName("Find event by Id - Success")
    public void testFindEventByIdSuccess(){
        //given
        Event testEvent = new Event("id", "testEvent", "testEventDescription", "Amsterdam", "John doe", new Date());
        given(eventsRepository.findById(any())).willReturn(Optional.of(testEvent));

        //when
        Optional<Event> findEvent = eventService.findEventById("id");

        //then

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(testEvent.getName())
                    .as("Check the name for match")
                    .isEqualTo(findEvent.get().getName());
            softly.assertThat(testEvent.getDescription())
                    .as("Check the description for match")
                    .isEqualTo(findEvent.get().getDescription());
        });
    }

    @Test
    @DisplayName("Test - getEventsByVenue success")
    public void testGetEventsByVenue(){
        //given
        Event testEvent = new Event("id", "testEvent", "testEventDescription", "Amsterdam", "John doe", new Date());
        mongoTemplate.save(testEvent);

        //when
        List<Event> findEvents = eventService.getEventsByVenue("Amsterdam");

        //then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(findEvents.size())
                    .as("Check count of the Events found")
                    .isEqualTo(1);
            softly.assertThat(findEvents.get(0).getVenue())
                    .as("Check if the Venue matches")
                    .isEqualTo("Amsterdam");
        });
    }

}