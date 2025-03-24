package com.example.hastaTakip;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static com.example.hastaTakip.messagebox.*;

public class DoctorLogin implements iLoginType {
    @Override
    public void handleLogin(String tc, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("doctor_info.txt"))) {
            String line;
            boolean loginSuccessful = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("T.C.: " + tc)) {
                    String passwordLine = reader.readLine();
                    if (passwordLine.trim().equals("Şifre: " + password)) {
                        loginSuccessful = true;
                        break;
                    }
                }
            }
            if (loginSuccessful) {
                showSuccessMessage("Giriş başarılı!");
                showDoktorEkrani();
            } else {
                showErrorMessage("Geçersiz kimlik bilgileri.");
            }
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }

    @FXML
    private void showDoktorEkrani() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("doktorEkrani-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Doktor Ekranı");
            stage.setScene(new Scene(root, 710, 930));
            stage.show();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }

}