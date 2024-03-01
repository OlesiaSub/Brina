package org.demo.texteditorwithai;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import static org.demo.texteditorwithai.Main.defaultHeight;
import static org.demo.texteditorwithai.Main.defaultWidth;

public class SignUpController extends SignInController {
    @FXML
    protected Button backButton;

    @FXML
    protected Button eyeButton;

    @FXML
    protected void initialize() {
        super.initialize();
    }

    @FXML
    protected void backButtonClicked() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        try {
            loadScene(stage, "sign-in-view.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void loadSuccessScene(Stage stage, String fxmlView) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource(fxmlView));
        Parent loader = fxmlloader.load();
        Scene scene = new Scene(loader, defaultWidth, defaultHeight);
        stage.setScene(scene);
    }

    @FXML
    protected void signUpButtonClicked() throws IOException {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        boolean isValid = checkIfEmpty();
        if (isValid) {
            loadSuccessScene(stage, "successful-sign-up-view.fxml");
            stage.setResizable(true);
        }
    }
}