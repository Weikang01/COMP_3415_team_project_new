package com.bean;

public class ResultMessage {
    private boolean isSystem;
    private String fromName;
    private Object message;

    public ResultMessage() {
    }

    public ResultMessage(boolean isSystem, String fromName, String message) {
        this.isSystem = isSystem;
        this.fromName = fromName;
        this.message = message;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
