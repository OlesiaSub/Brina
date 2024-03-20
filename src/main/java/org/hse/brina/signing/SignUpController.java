package org.hse.brina.signing;

import org.hse.brina.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * SignUpController управляет окном регистрации в приложение,
 * отображает ошибки при некорректном введении данных,
 * осуществляет переход на главное окно приложения при успешной регистрации.
 */
public class SignUpController extends SignInController {
    @FXML
    private Button backButton;

    @FXML
    protected void initialize() {
        super.initialize();
    }

    @FXML
    private void backButtonClicked() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        try {
            loadScene(stage, "/org/hse/brina/views/sign-in-view.fxml");
        } catch (IOException e) {
            System.out.println("Scene configuration file not found. " + e.getMessage());
        }
    }

    @FXML
    private void signUpButtonClicked() {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        boolean isValid = checkIfFieldsAreEmpty();
        String username = loginField.getText();
        String password = passwordField.getText();
        Client client = new Client("localhost", 8080);
        client.sendMessage("signUpUser " + username + " " + password);
        String response = client.receiveMessage();
        if (isValid) {
            try {
                if (response.equals("User with the same name already exists")) {
                    invalidLoginField.setText("User with this name already exists");
                    invalidLoginField.setVisible(true);
                } else if (response.equals("User is registered")) {
                    loadScene(stage, "/org/hse/brina/views/successful-sign-up-view.fxml");
                }
            } catch (IOException e) {
                System.err.println("Scene configuration file not found. " + e.getMessage());
            }
            stage.setResizable(true);
        }
    }
}