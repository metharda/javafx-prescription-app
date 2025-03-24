package com.example.hastaTakip;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patient {
    private final SimpleStringProperty tc;
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;
    private final SimpleIntegerProperty age;
    private final SimpleDoubleProperty weight;
    private final SimpleDoubleProperty height;
    private final SimpleStringProperty bloodGroup;
    private final SimpleStringProperty gender;

    public Patient(String tc, String name, String surname, int age, double weight, double height, String bloodGroup, String gender) {
        this.tc = new SimpleStringProperty(tc);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.age = new SimpleIntegerProperty(age);
        this.weight = new SimpleDoubleProperty(weight);
        this.height = new SimpleDoubleProperty(height);
        this.bloodGroup = new SimpleStringProperty(bloodGroup);
        this.gender = new SimpleStringProperty(gender);
    }

    public String getTc() {
        return tc.get();
    }
    public SimpleStringProperty tcProperty() {
        return tc;
    }
    public String getName() {
        return name.get();
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public String getSurname() {
        return surname.get();
    }
    public SimpleStringProperty surnameProperty() {
        return surname;
    }
    public int getAge() {
        return age.get();
    }
    public SimpleIntegerProperty ageProperty() {
        return age;
    }
    public double getWeight() {
        return weight.get();
    }
    public SimpleDoubleProperty weightProperty() {
        return weight;
    }
    public double getHeight() {
        return height.get();
    }
    public SimpleDoubleProperty heightProperty() {
        return height;
    }
    public String getBloodGroup() {
        return bloodGroup.get();
    }
    public SimpleStringProperty bloodGroupProperty() {
        return bloodGroup;
    }
    public String getGender() {
        return gender.get();
    }
    public SimpleStringProperty genderProperty() {
        return gender;
    }
}