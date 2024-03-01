package org.demo.texteditorwithai;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SuccessfulSignUpController {

    public Label label;

    @FXML
    protected void enter() {
        Stage stage = (Stage) label.getScene().getWindow();
        try {
            loadScene(stage, "main-window-view.fxml");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        stage.setResizable(true);
    }

    private void loadScene(Stage stage, String fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlView));
        Parent logInLoader = loader.load();
        int width = (int) stage.getWidth();
        int height = (int) stage.getHeight();
        Scene scene = new Scene(logInLoader, width, height);
        stage.setScene(scene);
    }

    @FXML
    public void initialize() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), label);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> enter());

        FadeTransition delay = new FadeTransition(Duration.seconds(1.5), label);
        delay.setFromValue(1.0);
        delay.setToValue(1.0);
        delay.setOnFinished(event -> fadeOut.play());

        delay.play();
    }
}