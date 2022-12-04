package com.bean;
import java.util.Date;


public class Appointment {
    private int id;
    private int resident_id;
    private int doctor_id;
    private Date date;
    private int hour;
    private int min;
    private int status;
    private String reason;

    public Appointment() {
    }

    public Appointment(int id, int resident_id, int doctor_id, Date date, int hour, int min, int status, String reason) {
        this.id = id;
        this.resident_id = resident_id;
        this.doctor_id = doctor_id;
        this.date = date;
        this.hour = hour;
        this.min = min;
        this.status = status;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResident_id() {
        return resident_id;
    }

    public void setResident_id(int resident_id) {
        this.resident_id = resident_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", resident_id=" + resident_id +
                ", doctor_id=" + doctor_id +
                ", date=" + date +
                ", hour=" + hour +
                ", min=" + min +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
