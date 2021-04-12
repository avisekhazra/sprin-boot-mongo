package com.spring.boot.demo.Service.impl;

import com.spring.boot.demo.Service.EventService;
import com.spring.boot.demo.model.Event;
import com.spring.boot.demo.repositories.EventsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class EventServiceImplTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private EventsRepository eventsRepository;

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

}