package com.bean;

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
    private int specialty1_id;
    private int specialty2_id;
    private int specialty3_id;
    private int specialty4_id;
    private int specialty5_id;
    private boolean is_active;
    private double distance;
    private String hospital;
    private String specialty1;
    private String specialty2;
    private String specialty3;
    private String specialty4;
    private String specialty5;


    public Doctor() {
    }

    public Doctor(int id, String username, String password, String firstname, String lastname, String address, double latitude, double longitude, String tel, int hospital_id, int specialty1_id, int specialty2_id, int specialty3_id, int specialty4_id, int specialty5_id, boolean is_active) {
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
        this.specialty1_id = specialty1_id;
        this.specialty2_id = specialty2_id;
        this.specialty3_id = specialty3_id;
        this.specialty4_id = specialty4_id;
        this.specialty5_id = specialty5_id;
        this.is_active = is_active;
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

    public int getSpecialty1_id() {
        return specialty1_id;
    }

    public void setSpecialty1_id(int specialty1_id) {
        this.specialty1_id = specialty1_id;
    }

    public int getSpecialty2_id() {
        return specialty2_id;
    }

    public void setSpecialty2_id(int specialty2_id) {
        this.specialty2_id = specialty2_id;
    }

    public int getSpecialty3_id() {
        return specialty3_id;
    }

    public void setSpecialty3_id(int specialty3_id) {
        this.specialty3_id = specialty3_id;
    }

    public int getSpecialty4_id() {
        return specialty4_id;
    }

    public void setSpecialty4_id(int specialty4_id) {
        this.specialty4_id = specialty4_id;
    }

    public int getSpecialty5_id() {
        return specialty5_id;
    }

    public void setSpecialty5_id(int specialty5_id) {
        this.specialty5_id = specialty5_id;
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

    public String getSpecialty1() {
        return specialty1;
    }

    public void setSpecialty1(String specialty1) {
        this.specialty1 = specialty1;
    }

    public String getSpecialty2() {
        return specialty2;
    }

    public void setSpecialty2(String specialty2) {
        this.specialty2 = specialty2;
    }

    public String getSpecialty3() {
        return specialty3;
    }

    public void setSpecialty3(String specialty3) {
        this.specialty3 = specialty3;
    }

    public String getSpecialty4() {
        return specialty4;
    }

    public void setSpecialty4(String specialty4) {
        this.specialty4 = specialty4;
    }

    public String getSpecialty5() {
        return specialty5;
    }

    public void setSpecialty5(String specialty5) {
        this.specialty5 = specialty5;
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
                ", specialty1_id=" + specialty1_id +
                ", specialty2_id=" + specialty2_id +
                ", specialty3_id=" + specialty3_id +
                ", specialty4_id=" + specialty4_id +
                ", specialty5_id=" + specialty5_id +
                ", is_active=" + is_active +
                '}';
    }
}
