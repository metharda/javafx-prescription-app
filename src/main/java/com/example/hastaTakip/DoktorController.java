package com.example.hastaTakip;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.*;

public class DoktorController {

    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> tcColumn;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, String> surnameColumn;
    @FXML
    private TableColumn<Patient, Integer> ageColumn;
    @FXML
    private TableColumn<Patient, String> genderColumn;
    @FXML
    private TableColumn<Patient, String> bloodColumn;
    @FXML
    private TableColumn<Patient, Double> weightColumn;
    @FXML
    private TableColumn<Patient, Double> heightColumn;
    @FXML
    private ComboBox<String> patientBox;
    private ObservableList<String> patientNames = FXCollections.observableArrayList();
    @FXML
    private Button deleteButton;
    @FXML
    private Button delAllButton;
    @FXML
    private Button updateButton;
    @FXML
    private ComboBox<String> medPatientBox;
    @FXML
    private TextArea diagnosis;
    @FXML
    private TextArea medGiven;
    @FXML
    private TextArea dosage;
    @FXML
    private TextArea usage;
    @FXML
    private TextArea date;
    @FXML
    private Button generatePdfButton;
    @FXML
    private ListView<String> nameList;
    @FXML
    private Button backButton;
    @FXML
    private ComboBox<String> presSelection;
    @FXML
    private Canvas presCanvas;
    @FXML
    private Button delSelPres;
    @FXML
    private Button delAllPres;
    @FXML
    public void initialize() {
        tcColumn.setCellValueFactory(new PropertyValueFactory<>("tc"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        bloodColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        patientBox.setItems(patientNames);
        medPatientBox.setItems(patientNames);
        setPatientTable("patient_info.txt");
        setDoctorInfo("doctor_info.txt");
        backButton.setOnAction(event -> redirectToLoginPage());
        generatePdfButton.setOnAction(event -> generatePdfForPatient());
        deleteButton.setOnAction(event -> deleteSelectedPatient());
        delAllButton.setOnAction(event -> deleteAllPatients());
        updateButton.setOnAction(event -> updatePatientList());
        File recetelerDir = new File("receteler");
        if (recetelerDir.exists() && recetelerDir.isDirectory()) {
            File[] files = recetelerDir.listFiles((dir, name) -> name.endsWith(".pdf"));
            if (files != null) {
                for (File file : files) {
                    presSelection.getItems().add(file.getName());
                }
            }
        }
        presSelection.setOnAction(event -> displaySelectedPrescription());
        delSelPres.setOnAction(event -> deleteSelectedPrescription());
        delAllPres.setOnAction(event -> deleteAllPrescriptions());
    }
    private void setDoctorInfo(String fileName) {
        ObservableList<String> doctorInfo = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().equals("Doktor Bilgileri:") && !line.startsWith("Şifre:")) {
                    doctorInfo.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!doctorInfo.isEmpty()) {
            String name = doctorInfo.stream()
                    .filter(info -> info.startsWith("Ad:"))
                    .map(info -> info.split(":")[1].trim())
                    .findFirst()
                    .orElse("Doktor");
            doctorInfo.add(0, "Hoşgeldiniz " + name+"!\n");
            doctorInfo.add(1, "\n");
        }
        nameList.setItems(doctorInfo);
    }
    private void setPatientTable(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String ad = "", soyad = "", tc = "", kanGrubu = "", cinsiyet = "";
            int yas = 0;
            double kilo = 0, boy = 0;
            Patient currentPatient = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("Hasta Bilgileri:")) {
                    if (currentPatient != null) {
                        patientTable.getItems().add(currentPatient);
                        patientNames.add(currentPatient.getName() + " " + currentPatient.getSurname());
                    }
                    currentPatient = null;
                } else if (line.startsWith("T.C.:")) {
                    tc = line.substring(6).trim();
                } else if (line.startsWith("Ad:")) {
                    ad = line.substring(4).trim();
                } else if (line.startsWith("Soyad:")) {
                    soyad = line.substring(7).trim();
                } else if (line.startsWith("Yaş:")) {
                    yas = Integer.parseInt(line.substring(5).trim());
                } else if (line.startsWith("Kilo:")) {
                    kilo = Double.parseDouble(line.substring(6).trim());
                } else if (line.startsWith("Boy:")) {
                    boy = Double.parseDouble(line.substring(5).trim());
                } else if (line.startsWith("Kan Grubu:")) {
                    kanGrubu = line.substring(11).trim();
                } else if (line.startsWith("Cinsiyet:")) {
                    cinsiyet = line.substring(10).trim();
                }
                if (ad != null && soyad != null && tc != null && yas != 0 && kilo != 0 && boy != 0 && kanGrubu != null && cinsiyet != null) {
                    currentPatient = new Patient(tc, ad, soyad, yas, kilo, boy, kanGrubu, cinsiyet);
                }
            }
            if (currentPatient != null) {
                patientTable.getItems().add(currentPatient);
                patientNames.add(currentPatient.getName() + " " + currentPatient.getSurname());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getSelectedPatientName() {
        return patientBox.getValue();
    }
    private void deleteSelectedPatient() {
        String selectedPatientName = getSelectedPatientName();
        if (selectedPatientName != null) {
            Patient selectedPatient = patientTable.getItems().stream()
                    .filter(patient -> (patient.getName() + " " + patient.getSurname()).equals(selectedPatientName))
                    .findFirst()
                    .orElse(null);
            if (selectedPatient != null) {
                patientTable.getItems().remove(selectedPatient);
                patientNames.remove(selectedPatientName);
                removePatientFromFile(selectedPatient);
            }
        }
    }
    private void removePatientFromFile(Patient patient) {
        try {
            File inputFile = new File("patient_info.txt");
            File tempFile = new File("temp_patient_info.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean skip = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("Hasta Bilgileri:")) {
                    skip = false;
                }
                if (line.startsWith("T.C.:") && line.substring(6).trim().equals(patient.getTc())) {
                    skip = true;
                }
                if (!skip && !line.trim().equals("Hasta Bilgileri:")) {
                    writer.write(line + System.lineSeparator());
                }
            }
            writer.close();
            reader.close();

            if (!inputFile.delete()) {
                System.out.println("Could not delete file");
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllPatients() {
        patientTable.getItems().clear();
        patientNames.clear();
        try {
            FileWriter writer = new FileWriter("patient_info.txt", false);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updatePatientList() {
        patientTable.getItems().clear();
        patientNames.clear();
        setPatientTable("patient_info.txt");
    }
    private void generatePdfForPatient() {
        String selectedPatientName = medPatientBox.getValue();
        if (selectedPatientName != null) {
            Patient selectedPatient = patientTable.getItems().stream()
                    .filter(patient -> (patient.getName() + " " + patient.getSurname()).equals(selectedPatientName))
                    .findFirst()
                    .orElse(null);
            if (selectedPatient != null) {
                String tc = selectedPatient.getTc();
                String diagnosisText = diagnosis.getText();
                String medGivenText = medGiven.getText();
                String dosageText = dosage.getText();
                String usageText = usage.getText();
                String dateText = date.getText();

                try {
                    File dir = new File("receteler");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    String pdfFileName = "receteler/" + tc + "_" + dateText + ".pdf";
                    Document document = new Document();
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
                    writer.open();
                    document.open();

                    document.add(new Paragraph("Tarih: " + dateText));
                    document.add(new Paragraph("Hasta TC'si: " + tc));
                    document.add(new Paragraph("Konulan Teshis: " + diagnosisText));
                    document.add(new Paragraph("Veirlen Ilac(lar): " + medGivenText));
                    document.add(new Paragraph("Doz Miktari: " + dosageText));
                    document.add(new Paragraph("Kullanım Sikligi: " + usageText));

                    document.close();
                    writer.close();
                    showSuccessMessage("Reçete oluşturuldu: " + pdfFileName);
                    presSelection.getItems().add(tc + "_" + dateText + ".pdf");
                    File healthDir = new File("saglik_gecmisi");
                    if (!healthDir.exists()) {
                        healthDir.mkdirs();
                    }
                    String txtFileName = "saglik_gecmisi/" + tc + "_" + dateText + "_" + diagnosisText + ".txt";
                    try (FileWriter txtWriter = new FileWriter(txtFileName)) {
                        txtWriter.write("Tarih: " + dateText + "\n");
                        txtWriter.write("Teşhis: " + diagnosisText + "\n");
                    }
                } catch (Exception e) {
                    showErrorMessage("Reçete oluşturulamadı: " + e.getMessage());
                }
            }
        }
    }
    private void displaySelectedPrescription() {
        String selectedPrescription = presSelection.getValue();
        if (selectedPrescription != null) {
            try {
                File pdfFile = new File("receteler", selectedPrescription);
                PDDocument document = Loader.loadPDF(pdfFile);
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                PDPage page = document.getPage(0);

                GraphicsContext gc = presCanvas.getGraphicsContext2D();
                presCanvas.setWidth(page.getMediaBox().getWidth());
                presCanvas.setHeight(page.getMediaBox().getHeight());

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void deleteSelectedPrescription() {
        String selectedPrescription = presSelection.getValue();
        if (selectedPrescription != null) {
            File pdfFile = new File("receteler", selectedPrescription);
            if (pdfFile.exists() && pdfFile.delete()) {
                presSelection.getItems().remove(selectedPrescription);
                presCanvas.getGraphicsContext2D().clearRect(0, 0, presCanvas.getWidth(), presCanvas.getHeight());
                showSuccessMessage("Seçilen reçete silindi.");
                String diagnosisFileName = selectedPrescription.replace(".pdf", ".txt");
                File diagnosisFile = new File("saglik_gecmisi", diagnosisFileName);
                if (diagnosisFile.exists()) {
                    diagnosisFile.delete();
                }
                showSuccessMessage("Seçilen reçete ve ilgili teşhis dosyası silindi.");
            } else {
                showErrorMessage("Reçete silinemedi.");
            }
        }
    }
    private void deleteAllPrescriptions() {
        File recetelerDir = new File("receteler");
        if (recetelerDir.exists() && recetelerDir.isDirectory()) {
            File[] files = recetelerDir.listFiles((dir, name) -> name.endsWith(".pdf"));
            if (files != null) {
                boolean allDeleted = true;
                for (File file : files) {
                    if (file.delete()) {
                        String diagnosisFileName = file.getName().replace(".pdf", ".txt");
                        File diagnosisFile = new File("saglik_gecmisi", diagnosisFileName);
                        if (diagnosisFile.exists()) {
                            diagnosisFile.delete();
                        }
                    } else {
                        allDeleted = false;
                    }
                }
                presSelection.getItems().clear();
                presCanvas.getGraphicsContext2D().clearRect(0, 0, presCanvas.getWidth(), presCanvas.getHeight());
                if (allDeleted) {
                    showSuccessMessage("Tüm reçeteler ve ilgili teşhis dosyaları silindi.");
                } else {
                    showErrorMessage("Bazı reçeteler veya teşhis dosyaları silinemedi.");
                }
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
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Başarılı");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Başarısız");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}