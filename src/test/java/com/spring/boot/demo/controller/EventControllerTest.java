package com.spring.boot.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.demo.Service.EventService;
import com.spring.boot.demo.model.Event;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Date;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @MockBean
    private EventService eventService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /events/{id} Test - Success")
    public void getEventByIdSuccess() throws Exception {
        //given

        Event testEvent = new Event("id", "testEvent", "testEventDescription", "Amsterdam", "John doe", new Date());
        given(eventService.findEventById(any())).willReturn(Optional.of(testEvent));
        //when

        mockMvc.perform(get("/events/{id}", "id"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testEvent"));


    }

    @Test
    @DisplayName("POST /events Test - Success")
    public void createEventSuccess() throws Exception {
        //Given
        Event testEvent = new Event("id", "testEvent", "testEventDescription", "Amsterdam", "John doe", new Date());
        given(eventService.postEvent(any())).willReturn(testEvent);
        //when
        mockMvc.perform(post("/events")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(testEvent)))
        //then
        .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("GET /events/{venue}")
    public void findEventsByVenue(){

    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}