package org.demo.texteditorwithai;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static org.demo.texteditorwithai.Main.*;

public class MainController{
    @FXML
    protected Label welcomeText;
    @FXML
    protected Button logInButton;
    @FXML
    protected Button registerButton;

    protected void loadScene(Stage stage, String fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlView));
        Parent logInLoader = loader.load();
        Scene scene = new Scene(logInLoader, defaultWidth, defaultHeight);
        stage.setScene(scene);
    }

    @FXML
    protected void logInButtonClicked() {
        Stage stage = (Stage) logInButton.getScene().getWindow();
        try {
            loadScene(stage, "log-in-view.fxml");
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void registerButtonClicked() {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        try {
            loadScene(stage, "register-view.fxml");
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}