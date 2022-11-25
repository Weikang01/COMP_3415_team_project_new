package com.bean;

import java.util.Date;

public class PatientHistoryForm {
    private int id;
    private int resident_id;
    private Date create_date;
    private String description;
    private boolean had_ECT;
    private boolean had_psychotherapy;
    private boolean had_drug_allergy;
    private String drug_allergy;

    public PatientHistoryForm() {
    }

    public PatientHistoryForm(int id, int resident_id, Date create_date, String description, boolean had_ECT, boolean had_psychotherapy, boolean had_drug_allergy, String drug_allergy) {
        this.id = id;
        this.resident_id = resident_id;
        this.create_date = create_date;
        this.description = description;
        this.had_ECT = had_ECT;
        this.had_psychotherapy = had_psychotherapy;
        this.had_drug_allergy = had_drug_allergy;
        this.drug_allergy = drug_allergy;
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

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHad_ECT() {
        return had_ECT;
    }

    public void setHad_ECT(boolean had_ECT) {
        this.had_ECT = had_ECT;
    }

    public boolean isHad_psychotherapy() {
        return had_psychotherapy;
    }

    public void setHad_psychotherapy(boolean had_psychotherapy) {
        this.had_psychotherapy = had_psychotherapy;
    }

    public boolean isHad_drug_allergy() {
        return had_drug_allergy;
    }

    public void setHad_drug_allergy(boolean had_drug_allergy) {
        this.had_drug_allergy = had_drug_allergy;
    }

    public String getDrug_allergy() {
        return drug_allergy;
    }

    public void setDrug_allergy(String drug_allergy) {
        this.drug_allergy = drug_allergy;
    }

    @Override
    public String toString() {
        return "PatientHistoryForm{" +
                "id=" + id +
                ", resident_id=" + resident_id +
                ", create_date=" + create_date +
                ", description='" + description + '\'' +
                ", had_ECT=" + had_ECT +
                ", had_psychotherapy=" + had_psychotherapy +
                ", had_drug_allergy=" + had_drug_allergy +
                ", drug_allergy='" + drug_allergy + '\'' +
                '}';
    }
}
