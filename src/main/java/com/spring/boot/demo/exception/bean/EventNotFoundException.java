package com.spring.boot.demo.exception.bean;

public class EventNotFoundException extends Exception{
    public EventNotFoundException(){
        super();
    }
    public EventNotFoundException(String message){
        super(message);
    }
}
