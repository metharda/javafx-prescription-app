package com.example.hastaTakip;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.example.hastaTakip.messagebox.*;

public class HastaKayitController {

    public Button backButton;
    // UI elements
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField tcField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField weightField;
    @FXML
    private TextField heightField;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<String> bloodTypeComboBox;
    private String patientTC;
    @FXML
    private void initialize() {
        backButton.setOnAction(event -> redirectToLoginPage());
        saveButton.setOnAction(event -> savePatientInfo());
    }

    private void savePatientInfo() {
        if (!nameField.getText().matches("[a-zA-Z]+") || !surnameField.getText().matches("[a-zA-Z]+")) {
            showErrorMessage("Ad ve soyad kısımları sadece harflerden oluşmalıdır.");
            return;
        }
        try {
            Integer.parseInt(tcField.getText());
            Integer.parseInt(ageField.getText());
            Integer.parseInt(weightField.getText());
            Integer.parseInt(heightField.getText());
        } catch (NumberFormatException e) {
            showErrorMessage("T.C., yaş, kilo ve boy kısımları sadece rakamlardan oluşmalıdır.");
            return;
        }
        String patientData = String.format(
                "Hasta Bilgileri:\n" +
                        "T.C.: %s\n" +
                        "Ad: %s\n" +
                        "Soyad: %s\n" +
                        "Cinsiyet: %s\n" +
                        "Yaş: %s\n" +
                        "Kan Grubu: %s\n" +
                        "Kilo: %s\n" +
                        "Boy: %s\n",
                tcField.getText(),
                nameField.getText(),
                surnameField.getText(),
                genderComboBox.getValue(),
                ageField.getText(),
                bloodTypeComboBox.getValue(),
                weightField.getText(),
                heightField.getText()
        );
        try (FileWriter writer = new FileWriter("patient_info.txt", true)) {
            writer.write(patientData);
            writer.write("**********\n");
            showSuccessMessage("Bilgileriniz başarıyla kaydedildi.");
            redirectToLoginPage();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }
    public void setPatientTC(String patientTC) {
        this.patientTC = patientTC;
    }
    public void loadPatientData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("patient_info.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("T.C.: " + patientTC)) {
                    tcField.setText(patientTC);
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().startsWith("Ad: ")) {
                            nameField.setText(line.substring(4).trim());
                        } else if (line.trim().startsWith("Soyad: ")) {
                            surnameField.setText(line.substring(7).trim());
                        }
                        if (line.trim().startsWith("**********")) {
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }
    private void redirectToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("giris-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setWidth(600);
            stage.setHeight(400);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }
}