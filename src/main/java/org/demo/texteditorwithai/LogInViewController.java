package org.demo.texteditorwithai;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInViewController extends MainController{

    @FXML
    protected Button backButton;

    @FXML
    protected void backButtonClicked(){
        Stage stage = (Stage) backButton.getScene().getWindow();
        try {
            loadScene(stage, "main-view.fxml");
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
