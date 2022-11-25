package com.bean;

public class CurrentDrugForm {
    private int id;
    private int resident_id;
    private String name;
    private String dose;
    private String how_long;

    public CurrentDrugForm() {
    }

    public CurrentDrugForm(int id, int resident_id, String name, String dose, String how_long) {
        this.id = id;
        this.resident_id = resident_id;
        this.name = name;
        this.dose = dose;
        this.how_long = how_long;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getHow_long() {
        return how_long;
    }

    public void setHow_long(String how_long) {
        this.how_long = how_long;
    }

    @Override
    public String toString() {
        return "DrugAllergyForm{" +
                "id=" + id +
                ", resident_id=" + resident_id +
                ", name='" + name + '\'' +
                ", dose='" + dose + '\'' +
                ", how_long='" + how_long + '\'' +
                '}';
    }
}
