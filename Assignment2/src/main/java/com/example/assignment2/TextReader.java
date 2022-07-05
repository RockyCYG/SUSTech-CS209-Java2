package com.example.assignment2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TextReader extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TextReader.class.getResource("TextReader.fxml"));
        Pane root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Text Reader Demo");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
