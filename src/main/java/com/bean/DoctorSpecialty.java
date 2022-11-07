package com.bean;

public class DoctorSpecialty {
    private int experience_level;
    private int specialty_id;
    private int doctor_id;

    public DoctorSpecialty() {
    }

    public DoctorSpecialty(int experience_level, int specialty_id, int doctor_id) {
        this.experience_level = experience_level;
        this.specialty_id = specialty_id;
        this.doctor_id = doctor_id;
    }

    public int getExperience_level() {
        return experience_level;
    }

    public void setExperience_level(int experience_level) {
        this.experience_level = experience_level;
    }

    public int getSpecialty_id() {
        return specialty_id;
    }

    public void setSpecialty_id(int specialty_id) {
        this.specialty_id = specialty_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    @Override
    public String toString() {
        return "DoctorSpecialty{" +
                "experience_level=" + experience_level +
                ", specialty_id=" + specialty_id +
                ", doctor_id=" + doctor_id +
                '}';
    }
}
