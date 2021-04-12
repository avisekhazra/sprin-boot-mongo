package com.spring.boot.demo.exception.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class ExceptionResponse {

    private String id;
    @Getter @Setter
    private String title;
    @Getter @Setter
    private String message;
    public ExceptionResponse(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


}
