package com.bean;

import java.util.Date;

public class Appointment {

    private int id;
    private int resident_id;
    private int doctor_id;
    private Date date;
    private int hour;
    private int min;

    public Appointment() {
    }

    public Appointment(int id, int resident_id, int doctor_id, Date date, int hour, int min) {
        this.id = id;
        this.resident_id = resident_id;
        this.doctor_id = doctor_id;
        this.date = date;
        this.hour = hour;
        this.min = min;
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

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", resident_id=" + resident_id +
                ", doctor_id=" + doctor_id +
                ", date=" + date +
                ", hour=" + hour +
                ", min=" + min +
                '}';
    }
}
