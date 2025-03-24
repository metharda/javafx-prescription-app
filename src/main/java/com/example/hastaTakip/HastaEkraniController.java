package com.example.hastaTakip;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.pdmodel.PDPage;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.nio.file.StandardCopyOption;
import static com.example.hastaTakip.messagebox.*;

public class HastaEkraniController {
    @FXML
    private Button backButton;
    @FXML
    private Canvas canvas;
    @FXML
    private ListView<String> nameList;
    @FXML
    private Button deleteButton;
    @FXML
    private Button downloadButton;
    @FXML
    private TextArea disease;
    @FXML
    private TextArea surgery;
    @FXML
    private TextArea medicine;
    @FXML
    private ListView<String> history;
    @FXML
    private Button saveButton;
    @FXML
    private TableView<Diagnosis> diagnosisView;
    @FXML
    private TableColumn<Diagnosis, String> dateColumn;
    @FXML
    private TableColumn<Diagnosis, String> diagnosisColumn;
    @FXML
    private ComboBox<String> presSelect;
    @FXML
    public void initialize() {
        try {
            String patientTC = GirisController.getPatientTC();
            backButton.setOnAction(event -> redirectToLoginPage());
            deleteButton.setOnAction(event -> deletePatient(patientTC));
            downloadButton.setOnAction(event -> downloadSelectedPdf());
            saveButton.setOnAction(event -> saveHealthHistory(patientTC));
            presSelect.setOnAction(event -> displaySelectedPDF());
            dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            diagnosisColumn.setCellValueFactory(cellData -> cellData.getValue().diagnosisProperty());
            createPatientList(patientTC);
            loadHealthHistory(patientTC);
            loadDiagnosisData(GirisController.getPatientTC());
            listPatientPDFs(patientTC);
        } catch (Exception e) {
            showErrorMessage("Hasta ekranı yüklenemedi: " + e.getMessage());
        }
    }
    private void createPatientList(String tc) {
        try (BufferedReader reader = new BufferedReader(new FileReader("patient_info.txt"))) {
            String line;
            StringBuilder patientInfo = new StringBuilder();
            boolean isPatientFound = false;
            String patientName = "";
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("T.C.: " + tc)) {
                    patientInfo.append(line).append("\n");
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().startsWith("Ad: ")) {
                            patientName = line.substring(4).trim();
                        }
                        if (line.trim().startsWith("**********")) {
                            break;
                        }
                        patientInfo.append(line).append("\n");
                    }
                    isPatientFound = true;
                    break;
                }
            }
            if (isPatientFound) {
                nameList.getItems().add("Hoş geldiniz, " + patientName + "!");
                nameList.getItems().add("\n");
                nameList.getItems().add(patientInfo.toString().trim());
            } else {
                nameList.getItems().add("Hasta bulunamadı.");
            }
        } catch (IOException e) {
            showErrorMessage("Hasta listesi oluşturulamadı: " + e.getMessage());
        }
    }
    private void loadDiagnosisData(String patientTC) {
        File dir = new File("saglik_gecmisi");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.startsWith(patientTC + "_") && name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        StringBuilder diagnosisData = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            diagnosisData.append(line).append("\n");
                        }
                        String[] data = diagnosisData.toString().split("\n");
                        if (data.length >= 2) {
                            String date = data[0].substring(7); // Assuming "Tarih: " prefix
                            String diagnosis = data[1].substring(8); // Assuming "Teşhis: " prefix
                            diagnosisView.getItems().add(new Diagnosis(date, diagnosis));
                        }
                    } catch (IOException e) {
                        showErrorMessage("Teşhis verileri yüklenemedi: " + e.getMessage());
                    }
                }
            }
        }
    }
    private void listPatientPDFs(String patientTC) {
        File dir = new File("receteler");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.startsWith(patientTC + "_") && name.endsWith(".pdf"));
            if (files != null) {
                for (File file : files) {
                    presSelect.getItems().add(file.getName());
                }
            }
        }
    }
    private void displaySelectedPDF() {
        String selectedPDF = presSelect.getValue();
        if (selectedPDF != null) {
            try {
                createPdfImage("receteler/" + selectedPDF);
            } catch (IOException e) {
                showErrorMessage("PDF görüntülenemedi: " + e.getMessage());
            }
        }
    }
    private void createPdfImage(String pdfFilePath) throws IOException {
        if (pdfFilePath!=null){
            File pdfFile = new File(pdfFilePath);
            PDDocument document = Loader.loadPDF(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            PDPage page = document.getPage(0);

            GraphicsContext gc = canvas.getGraphicsContext2D();
            canvas.setWidth(page.getMediaBox().getWidth());
            canvas.setHeight(page.getMediaBox().getHeight());

            BufferedImage image = pdfRenderer.renderImageWithDPI(0, 70);
            WritableImage fxImage = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = fxImage.getPixelWriter();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
            gc.drawImage(fxImage, 0, 0);
            document.close();
        }
    }
    private void downloadSelectedPdf() {
        String selectedPDF = presSelect.getValue();
        if (selectedPDF != null) {
            downloadPdf("receteler/" + selectedPDF);
        } else {
            showErrorMessage("Lütfen indirilecek bir reçete seçin.");
        }
    }
    private void downloadPdf(String pdfFilePath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName(pdfFilePath);

        Stage stage = (Stage) downloadButton.getScene().getWindow();
        File destFile = fileChooser.showSaveDialog(stage);

        if (destFile != null) {
            try {
                Files.copy(new File(pdfFilePath).toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                showSuccessMessage("PDF indirme başarılı.");
            } catch (IOException e) {
                showErrorMessage("PDF indirme başarısız: " + e.getMessage());
            }
        }
    }

    private void deletePatient(String tc) {
        try {
            File file = new File("patient_info.txt");
            File tempFile = new File("temp_patient_info.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder patientInfo = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("T.C.: " + tc)) {
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().startsWith("**********")) {
                            break;
                        }
                    }
                } else {
                    patientInfo.append(line).append("\n");
                }
            }
            reader.close();
            file.delete();
            tempFile.createNewFile();
            java.io.FileWriter fw = new java.io.FileWriter(tempFile);
            fw.write(patientInfo.toString());
            fw.close();
            tempFile.renameTo(file);
            showSuccessMessage("Kaydınız başarıyla silindi.");
            redirectToLoginPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveHealthHistory(String patientTC) {
        String healthHistory = String.format(
                "Hastalık: %s\nAmeliyat: %s\nİlaç: %s\n",
                disease.getText(),
                surgery.getText(),
                medicine.getText()
        );

        File dir = new File("saglik_gecmisi");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, patientTC + ".txt");
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(healthHistory + "**********\n");
            showSuccessMessage("Sağlık geçmişi kaydedildi.");
            loadHealthHistory(patientTC);
        } catch (IOException e) {
            showErrorMessage("Sağlık geçmişi kaydedilemedi: " + e.getMessage());
        }
    }

    private void loadHealthHistory(String patientTC) {
        File file = new File("saglik_gecmisi", patientTC + ".txt");
        history.getItems().clear();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().equals("**********")) {
                        history.getItems().add(line);
                    }
                }
            } catch (IOException e) {
                showErrorMessage("Sağlık geçmişi yüklenemedi: " + e.getMessage());
            }
        }
    }

    private void redirectToLoginPage() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}

