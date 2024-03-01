package org.demo.texteditorwithai;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.demo.texteditorwithai.Main.defaultHeight;
import static org.demo.texteditorwithai.Main.defaultWidth;

public class FirstViewController implements Initializable {
    @FXML
    public Label text;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), text);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> openWelcomeWindow());

        FadeTransition delay = new FadeTransition(Duration.seconds(2), text);
        delay.setFromValue(1.0);
        delay.setToValue(1.0);
        delay.setOnFinished(event -> fadeOut.play());

        delay.play();
    }
    protected void loadScene(Stage stage, String fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlView));
        Parent logInLoader = loader.load();
        Scene scene = new Scene(logInLoader, defaultWidth, defaultHeight);
        stage.setScene(scene);
    }

    protected void openWelcomeWindow() {
        Stage stage = (Stage) text.getScene().getWindow();
        try {
            loadScene(stage, "sign-in-view.fxml");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}