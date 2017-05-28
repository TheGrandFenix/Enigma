package com.fenix.enigma;

//Class for storing message data for the database
public class Message {
    public String name;
    public String message;

    public Message() {

    }

    public Message(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
