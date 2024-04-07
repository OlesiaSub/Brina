package org.hse.brina.signing;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hse.brina.Config;
import org.hse.brina.client.Client;

import java.io.IOException;

/**
 * SignUpController управляет окном регистрации в приложение,
 * отображает ошибки при некорректном введении данных,
 * осуществляет переход на главное окно приложения при успешной регистрации.
 */
public class SignUpController extends SignInController {
    private static final Logger logger = LogManager.getLogger();
    public VBox passwordRulesVBox;
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
            logger.error("Scene configuration file not found. " + e.getMessage());
        }
    }

    @FXML
    private void signUpButtonClicked() {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        boolean isValid = checkIfFieldsAreEmpty();
        String username = loginField.getText();
        String password = passwordField.getText();
        String pattern = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@#$%]).{8,40}";
        if (!password.matches(pattern)) {
            passwordRulesVBox.getChildren().clear();
            Text passwordRules = new Text();
            passwordRules.setText("Weak password. The password must contain a lowercase and\nan uppercase letter, as well as a number or special character");
            passwordRules.setFill(Color.valueOf("#e0850c"));
            passwordRulesVBox.getChildren().add(passwordRules);
            Text emptyText = new Text();
            emptyText.setText(" ");
            passwordRulesVBox.getChildren().add(emptyText);
            isValid = false;
        } else {
            isValid = true;
            passwordRulesVBox.getChildren().removeAll();
        }
        if (isValid) {
            Config.client.setName(username);
            Config.client.sendMessage("signUpUser " + username + " " + password);
            String response = Config.client.receiveMessage();
            try {
                if (response.equals("User with the same name already exists")) {
                    invalidLoginField.setText("User with this name already exists");
                    invalidLoginField.setVisible(true);
                } else if (response.equals("User is registered")) {
                    loadScene(stage, "/org/hse/brina/views/successful-sign-up-view.fxml");
                }
            } catch (Exception e) {
                logger.error("Scene configuration file not found. " + e.getMessage());
            }
            stage.setResizable(true);
        }
    }
}