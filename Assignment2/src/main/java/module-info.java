module com.example.assignment2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.example.assignment2 to javafx.fxml;
    exports com.example.assignment2;
}