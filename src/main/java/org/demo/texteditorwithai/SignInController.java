package org.demo.texteditorwithai;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static org.demo.texteditorwithai.Main.*;

public class SignInController {
    @FXML
    protected Button eyeButton;
    @FXML
    protected TextField loginField;
    @FXML
    protected PasswordField passwordField;
    @FXML
    protected TextField invalidLoginField;
    @FXML
    protected TextField invalidPasswordField;
    @FXML
    protected TextField openedPasswordField;
    @FXML
    protected ImageView eyeImage;
    @FXML
    protected Label welcomeText;
    @FXML
    protected Button signInButton;
    @FXML
    protected Button signUpButton;

    private boolean passwordVisible = false;

    @FXML
    protected void initialize() {

        Image eyeOpen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("open-eye.png")));
        Image eyeClosed = new Image(Objects.requireNonNull(getClass().getResourceAsStream("closed-eye.png")));

        eyeButton.setOnAction(event -> {
            if (!passwordVisible) {
                String passwordOpen = passwordField.getText();
                openedPasswordField.setVisible(true);
                openedPasswordField.requestFocus();
                openedPasswordField.setText(passwordOpen);
                openedPasswordField.positionCaret(passwordOpen.length());
                passwordField.setVisible(false);
                eyeImage.setImage(eyeOpen);
                passwordVisible = !passwordVisible;
            } else {
                String passwordOpen = openedPasswordField.getText();
                passwordField.setVisible(true);
                passwordField.requestFocus();
                passwordField.setText(passwordOpen);
                passwordField.positionCaret(passwordOpen.length());
                openedPasswordField.setVisible(false);
                eyeImage.setImage(eyeClosed);
                passwordVisible = !passwordVisible;

            }
        });

        invalidLoginField.setOnMouseClicked(event -> {
            if (invalidLoginField.isVisible()) {
                invalidLoginField.setVisible(false);
            }
        });

        invalidPasswordField.setOnMouseClicked(event -> {
            if (invalidPasswordField.isVisible()) {
                invalidPasswordField.setVisible(false);
            }
        });

    }

    protected void loadScene(Stage stage, String fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlView));
        Parent logInLoader = loader.load();
        Scene scene = new Scene(logInLoader, defaultWidth, defaultHeight);
        stage.setScene(scene);
    }

    public void enter(Stage stage) {
        try {
            loadScene(stage, "main-window-view.fxml");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        stage.setResizable(true);
    }

    @FXML
    protected boolean checkIfEmpty() {
        boolean isValid = true;
        eyeButton.setOnAction(Event::consume);
        if (loginField.getText().isEmpty()) {
            invalidLoginField.setVisible(true);
            isValid = false;
        } else {
            invalidLoginField.setVisible(false);
        }
        if (passwordField.getText().isEmpty()) {
            invalidPasswordField.setVisible(true);
            isValid = false;
        } else {
            invalidPasswordField.setVisible(false);
        }
        return isValid;
    }

    @FXML
    protected void signInButtonClicked() {
        Stage stage = (Stage) signInButton.getScene().getWindow();
        boolean isValid = checkIfEmpty();

    /*TODO: catch errors and make errors visible for the user --> then enter()
    1) unknown login
    2) wrong password

    if(login is not found in DB){

    } else {

    }

    if(password is wrong for the login){

    } else {

    }
     */

        if (isValid) {
            enter(stage);
        }

    }

    @FXML
    protected void signUpFromInButtonClicked() {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        try {
            loadScene(stage, "sign-up-view.fxml");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}