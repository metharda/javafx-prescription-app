package com.example.hastaTakip;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static com.example.hastaTakip.messagebox.*;

import java.io.IOException;

public class GirisController {
    @FXML
    private Button doctorButton;
    @FXML
    private Button patientButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label secondLabel;
    @FXML
    public Label signupLabel;
    @FXML
    private TextField tcTextField;
    @FXML
    private TextField secondField;
    private iLoginType loginType;
    private static String patientTC;
    private static String doctorTC;

    @FXML
    public void initialize() {
        secondLabel.setText("Şifre");
        signupLabel.setVisible(false);
        doctorButton.setDefaultButton(true);
        patientButton.setDefaultButton(false);

        doctorButton.setOnAction(event -> {
            secondLabel.setText("Şifre");
            secondLabel.setVisible(true);
            secondField.setVisible(true);
            signupLabel.setVisible(false);
            doctorButton.setDefaultButton(true);
            patientButton.setDefaultButton(false);
            setLoginType(new DoctorLogin());
        });

        patientButton.setOnAction(event -> {
            secondLabel.setVisible(false);
            signupLabel.setVisible(true);
            secondField.setVisible(false);
            patientButton.setDefaultButton(true);
            doctorButton.setDefaultButton(false);
            setLoginType(new PatientLogin());
        });

        doctorButton.fire();
        signupLabel.setOnMouseClicked(event -> openSignupScreen());
        signupLabel.setOnMouseEntered(event -> signupLabel.setStyle("-fx-underline: true;"));
        signupLabel.setOnMouseExited(event -> signupLabel.setStyle("-fx-underline: false;"));
        loginButton.setOnMouseClicked(event -> handleLogin());
    }
    private void DoctorTC() {
        doctorTC = tcTextField.getText();
    }
    public static String getDoctorTC() {
        return doctorTC;
    }
    public void setDoctorTC(String doctorTC) {
        GirisController.doctorTC = doctorTC;
    }
    private void PatientTC() {
        patientTC = tcTextField.getText();
    }
    public static String getPatientTC() {
        return patientTC;
    }
    public void setPatientTC(String patientTC) {
        GirisController.patientTC = patientTC;
    }

    private void setLoginType(iLoginType loginType) {
        this.loginType = loginType;
    }
    private void handleLogin() {
        PatientTC();
        DoctorTC();
        String tc = tcTextField.getText();
        String secondInput = secondField.getText();
        loginType.handleLogin(tc, secondInput);
    }
    public void openSignupScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hastaKayit-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signupLabel.getScene().getWindow();
            stage.setWidth(700);
            stage.setHeight(550);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }
}
