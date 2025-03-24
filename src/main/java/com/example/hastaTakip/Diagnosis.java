package com.example.hastaTakip;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Diagnosis {
    private final StringProperty date;
    private final StringProperty diagnosis;

    public Diagnosis(String date, String diagnosis) {
        this.date = new SimpleStringProperty(date);
        this.diagnosis = new SimpleStringProperty(diagnosis);
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty diagnosisProperty() {
        return diagnosis;
    }
}