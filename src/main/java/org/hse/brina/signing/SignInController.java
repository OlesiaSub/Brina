package org.hse.brina.signing;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import org.hse.brina.Config;
import org.hse.brina.Main;

import org.hse.brina.client.Client;

import java.io.IOException;
import java.util.Objects;

/**
 * SignInController управляет окно входа в приложение,
 * проверяет соответствие логина и пароля уже зарегистрированного пользователя,
 * отображает ошибки при некорректном введении данных,
 * осуществляет переход на главное окно приложения при успешной авторизации.
 */
public class SignInController {

    @FXML
    protected Label welcomeText;
    @FXML
    protected Button signInButton;
    @FXML
    protected Button signUpButton;
    @FXML
    protected TextField loginField;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected TextField invalidLoginField;
    @FXML
    protected TextField invalidPasswordField;
    @FXML
    private Button eyeButton;
    @FXML
    private TextField openedPasswordField;
    @FXML
    private ImageView eyeImage;

    private boolean passwordVisible = false;


    @FXML
    protected void initialize() {

        Image eyeOpen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/hse/brina/assets/open-eye.png")));
        Image eyeClosed = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/hse/brina/assets/closed-eye.png")));

        eyeButton.setOnAction(event -> {
            if (!passwordVisible) {
                changePasswordVisibility(eyeOpen, passwordField, openedPasswordField);
            } else {
                changePasswordVisibility(eyeClosed, openedPasswordField, passwordField);

            }
        });

        invalidLoginField.setOnMouseClicked(event -> hideWarningAboutEmptyField(invalidLoginField));

        invalidPasswordField.setOnMouseClicked(event -> hideWarningAboutEmptyField(invalidPasswordField));

    }

    protected void loadScene(Stage stage, String fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlView));
        Parent logInLoader = loader.load();
        Scene scene = new Scene(logInLoader, Config.getDefaultWidth(), Config.getDefaultHeight());
        stage.setScene(scene);
    }

    @FXML
    protected boolean checkIfFieldsAreEmpty() {
        boolean isValid = true;
        eyeButton.setOnAction(Event::consume);
        if (loginField.getText().isEmpty()) {
            invalidLoginField.setText("Enter your login");
            invalidLoginField.setVisible(true);
            isValid = false;
        } else {
            invalidLoginField.setVisible(false);
        }
        if (passwordField.getText().isEmpty()) {
            invalidPasswordField.setText("Enter your password");
            invalidPasswordField.setVisible(true);
            isValid = false;
        } else {
            invalidPasswordField.setVisible(false);
        }
        return isValid;
    }

    @FXML
    private void signInButtonClicked() {
        Stage stage = (Stage) signInButton.getScene().getWindow();
        boolean isValid = checkIfFieldsAreEmpty();
        String username = loginField.getText();
        String password = passwordField.getText();

        Client client = new Client("localhost", 8080);
        client.sendMessage("signInUser " + username + " " + password);
        String response =  client.receiveMessage();
        if (response.equals("User with this name not found")) {
            invalidLoginField.setText("User with this name doesn't exist");
            invalidLoginField.setVisible(true);
        } else if (response.equals("Wrong password")) {
            invalidPasswordField.setText("Wrong password");
            invalidPasswordField.setVisible(true);
        } else if (isValid && response.equals("User logged in")) {
            enter(stage);
        }
    }

    @FXML
    private void signUpFromInButtonClicked() {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        try {
            loadScene(stage, "/org/hse/brina/views/sign-up-view.fxml");
        } catch (IOException e) {
            System.err.println("Scene configuration file not found. " + e.getMessage());
        }
    }

    private void enter(Stage stage) {
        try {
            loadScene(stage, "/org/hse/brina/views/main-window-view.fxml");
        } catch (IOException e) {
            System.err.println("Scene configuration file not found. " + e.getMessage());
        }
        stage.setResizable(true);
    }

    private void hideWarningAboutEmptyField(TextField field) {
        if (field.isVisible()) {
            field.setVisible(false);
        }
    }

    private void changePasswordVisibility(Image eyeClosed, TextField openedPasswordField, TextField passwordField) {
        String passwordOpen = openedPasswordField.getText();
        passwordField.setVisible(true);
        passwordField.requestFocus();
        passwordField.setText(passwordOpen);
        passwordField.positionCaret(passwordOpen.length());
        openedPasswordField.setVisible(false);
        eyeImage.setImage(eyeClosed);
        passwordVisible = !passwordVisible;
    }

}