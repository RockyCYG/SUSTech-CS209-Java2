module com.example.lab7 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.lab7 to javafx.fxml;
    exports com.example.lab7;
}