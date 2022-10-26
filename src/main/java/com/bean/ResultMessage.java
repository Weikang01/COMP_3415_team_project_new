package com.bean;

public class ResultMessage {
    private boolean isSystem;
    private int fromId;
    private Object message;

    public ResultMessage() {
    }

    public ResultMessage(boolean isSystem, int fromId, String message) {
        this.isSystem = isSystem;
        this.fromId = fromId;
        this.message = message;
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
}
