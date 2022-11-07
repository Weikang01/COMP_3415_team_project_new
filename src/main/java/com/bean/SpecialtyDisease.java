package com.bean;

public class SpecialtyDisease {
    private int specialty_id;
    private int disease_id;

    public SpecialtyDisease() {
    }

    public SpecialtyDisease(int specialty_id, int disease_id) {
        this.specialty_id = specialty_id;
        this.disease_id = disease_id;
    }

    public int getSpecialty_id() {
        return specialty_id;
    }

    public void setSpecialty_id(int specialty_id) {
        this.specialty_id = specialty_id;
    }

    public int getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(int disease_id) {
        this.disease_id = disease_id;
    }
}
