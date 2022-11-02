package com.bean;

public class Message {
    private int toId;
    private boolean toResident;
    private String message;

    public Message() {
    }

    public Message(int toId, boolean toResident, String message) {
        this.toId = toId;
        this.toResident = toResident;
        this.message = message;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public boolean isToResident() {
        return toResident;
    }

    public void setToResident(boolean toResident) {
        this.toResident = toResident;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "toId=" + toId +
                ", toResident=" + toResident +
                ", message='" + message + '\'' +
                '}';
    }
}
