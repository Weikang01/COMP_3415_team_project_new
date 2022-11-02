package com.bean;

public class ResultMessage {
    private boolean isSystem;
    private int fromId;
    private Object message;
    private String firstname;
    private String lastname;

    public ResultMessage() {
    }

    public ResultMessage(boolean isSystem, int fromId, Object message, String firstname, String lastname) {
        this.isSystem = isSystem;
        this.fromId = fromId;
        this.message = message;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "ResultMessage{" +
                "isSystem=" + isSystem +
                ", fromId=" + fromId +
                ", message=" + message +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
