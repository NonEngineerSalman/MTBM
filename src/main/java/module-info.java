module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires itextpdf;
    requires javafx.swing;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}