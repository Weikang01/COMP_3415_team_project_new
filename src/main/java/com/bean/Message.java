package com.bean;

public class Message {
    private String toUsername;
    private String message;

    public Message() {
    }

    public Message(String toUsername, String message) {
        this.toUsername = toUsername;
        this.message = message;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
