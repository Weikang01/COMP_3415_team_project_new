package com.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Doctor Bean
 * @author: Weikang
 * @version: 1.0
 * @date: 29/09/2022 10:49
 */

public class Doctor {
    private int id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String address;
    private double latitude;
    private double longitude;
    private String tel;
    private int hospital_id;
    private boolean is_active;
    private double distance;
    private String hospital;
    private int specialty_index = 0;
    private Map<String, String> specialties = new HashMap<>();

    public Doctor() {
    }

    public Doctor(int id, String username, String password, String firstname, String lastname, String address, double latitude, double longitude, String tel, int hospital_id, boolean is_active, double distance, String hospital, Map<String, String> specialties) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tel = tel;
        this.hospital_id = hospital_id;
        this.is_active = is_active;
        this.distance = distance;
        this.hospital = hospital;
        this.specialties = specialties;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(int hospital_id) {
        this.hospital_id = hospital_id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public Map<String, String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Map<String, String> specialties) {
        this.specialties = specialties;
    }

    public void addSpecialty(String specialty) {
        this.specialties.put("" + specialty_index++, specialty);
        this.specialties.put("length", "" + specialty_index);
    }

    public String specialties_string() {
        StringBuilder r = new StringBuilder("{");
        for (String specialty : specialties.values()) {
            r.append(specialty);
            r.append(",");
        }
        r.append("}");
        return r.toString();
    }
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", tel='" + tel + '\'' +
                ", hospital_id=" + hospital_id +
                ", is_active=" + is_active +
                ", distance=" + distance +
                ", hospital='" + hospital + '\'' +
                ", specialties='" + specialties_string() +
        '}';
    }
}
