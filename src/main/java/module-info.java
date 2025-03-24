module com.example.hastaTakip {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires itextpdf;
    requires kernel;
    requires layout;

    opens com.example.hastaTakip to javafx.fxml;
    exports com.example.hastaTakip;
}