/**
 * SignUpController управляет окном регистрации в приложение,
 * отображает ошибки при некорректном введении данных,
 * осуществляет переход на главное окно приложения при успешной регистрации.
 */
package org.hse.texteditorwithai.signing;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

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
            loadScene(stage, "/org/hse/texteditorwithai/views/sign-in-view.fxml");
        } catch (IOException e) {
            System.out.println("Scene configuration file not found. " + e.getMessage());
        }
    }

    @FXML
    private void signUpButtonClicked() {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        boolean isValid = checkIfFieldsAreEmpty();
        if (isValid) {
            try{
                loadScene(stage, "/org/hse/texteditorwithai/views/successful-sign-up-view.fxml");
            } catch (IOException e){
                System.err.println("Scene configuration file not found. " + e.getMessage());
            }
            stage.setResizable(true);
        }
    }
}