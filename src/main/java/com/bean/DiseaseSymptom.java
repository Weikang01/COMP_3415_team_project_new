package com.bean;

public class DiseaseSymptom {
    private int disease_id;
    private int symptom_id;
    private int correlativity;

    public DiseaseSymptom() {
    }

    public DiseaseSymptom(int disease_id, int symptom_id, int correlativity) {
        this.disease_id = disease_id;
        this.symptom_id = symptom_id;
        this.correlativity = correlativity;
    }

    public int getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(int disease_id) {
        this.disease_id = disease_id;
    }

    public int getSymptom_id() {
        return symptom_id;
    }

    public void setSymptom_id(int symptom_id) {
        this.symptom_id = symptom_id;
    }

    public int getCorrelativity() {
        return correlativity;
    }

    public void setCorrelativity(int correlativity) {
        this.correlativity = correlativity;
    }
}
