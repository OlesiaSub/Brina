package org.hse.brina.signing;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hse.brina.Main;

import java.io.IOException;

/**
 * SuccessfulSignUpController осуществляет переход с приветственной надписью для новых пользователей
 * на главное окно приложения после успешной регистрации.
 */
public class SuccessfulSignUpController {

    private static final Logger logger = LogManager.getLogger();
    @FXML
    private Label label;

    @FXML
    protected void initialize() {
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

    private void enter() {
        Stage stage = (Stage) label.getScene().getWindow();
        try {
            loadScene(stage, "/org/hse/brina/views/main-window-view.fxml");
        } catch (IOException e) {
            logger.error("Scene configuration file not found. " + e.getMessage());
        }
        stage.setResizable(true);
    }

    private void loadScene(Stage stage, String fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlView));
        Parent logInLoader = loader.load();
        Scene scene = new Scene(logInLoader, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }
}
