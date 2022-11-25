package com.bean;

import java.util.Date;

public class FamilyHistoryForm {
    private int id;
    private int resident_id;
    private Date create_date;
    private String relation;
    private int age;
    private String health_condition;
    private boolean is_deceased;
    private int death_age;
    private  String death_cause;

    public FamilyHistoryForm() {
    }

    public FamilyHistoryForm(int id, int resident_id, Date create_date, String relation, int age, String health_condition, boolean is_deceased, int death_age, String death_cause) {
        this.id = id;
        this.resident_id = resident_id;
        this.create_date = create_date;
        this.relation = relation;
        this.age = age;
        this.health_condition = health_condition;
        this.is_deceased = is_deceased;
        this.death_age = death_age;
        this.death_cause = death_cause;
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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHealth_condition() {
        return health_condition;
    }

    public void setHealth_condition(String health_condition) {
        this.health_condition = health_condition;
    }

    public boolean isIs_deceased() {
        return is_deceased;
    }

    public void setIs_deceased(boolean is_deceased) {
        this.is_deceased = is_deceased;
    }

    public int getDeath_age() {
        return death_age;
    }

    public void setDeath_age(int death_age) {
        this.death_age = death_age;
    }

    public String getDeath_cause() {
        return death_cause;
    }

    public void setDeath_cause(String death_cause) {
        this.death_cause = death_cause;
    }

    @Override
    public String toString() {
        return "FamilyHistoryForm{" +
                "id=" + id +
                ", resident_id=" + resident_id +
                ", create_date=" + create_date +
                ", relation='" + relation + '\'' +
                ", age=" + age +
                ", health_condition='" + health_condition + '\'' +
                ", is_deceased=" + is_deceased +
                ", death_age=" + death_age +
                ", death_cause='" + death_cause + '\'' +
                '}';
    }
}
