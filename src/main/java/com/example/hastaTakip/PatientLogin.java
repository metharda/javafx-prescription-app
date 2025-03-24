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

public class PatientLogin implements iLoginType{
    @Override
    public void handleLogin(String tc, String secondInput) {
        try (BufferedReader reader = new BufferedReader(new FileReader("patient_info.txt"))) {
            String line;
            boolean loginSuccessful = false;

            while ((line = reader.readLine()) != null){
                if (line.trim().equals("T.C.: " + tc)){
                    loginSuccessful = true;
                    break;
                }
            }
            if (loginSuccessful){
                showSuccessMessage("Giriş başarılı!");
                showHastaEkrani();
            }
            else
                showErrorMessage("Geçersiz kimlik bilgileri.");
            }
        catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }
    @FXML
    private void showHastaEkrani(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hastaEkrani-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Hasta Ekranı");
            stage.setScene(new Scene(root, 800, 880));
            stage.show();
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }
}