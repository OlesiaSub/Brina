package org.hse.brina.utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hse.brina.Main;
import org.hse.brina.richtext.RichTextDemo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainWindowController управляет главным окном приложения,
 * отображает документы, доступные пользователю.
 */
public class MainWindowController implements Initializable {
    @FXML
    public HBox imageHBox;
    @FXML
    private Button Enter;
    @FXML
    private TextField EnterIdField;
    @FXML
    private Button collab;
    @FXML
    private Button create_new;
    @FXML
    private Button my;
    private static final Logger logger = LogManager.getLogger();


    private void openButton(Stage stage, String fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlView));
        try {
            Parent sceneLoader = loader.load();
            Scene scene = new Scene(sceneLoader, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
        } catch (IOException e){
            logger.error("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openCreateButton() {
        Stage stage = (Stage) create_new.getScene().getWindow();
        RichTextDemo richTextWindow = new RichTextDemo();
        richTextWindow.start(stage);
    }


    @FXML
    private void openCollabButton() {
        Stage stage = (Stage) collab.getScene().getWindow();
        try {
            openButton(stage, "/views/collab-view.fxml");
        } catch (IOException e) {
            logger.error("Scene configuration file not found. " + e.getMessage());
        }
    }

    @FXML
    private void openMyProdButton() {
        Stage stage = (Stage) my.getScene().getWindow();
        try {
            openButton(stage, "/views/my-projects-view.fxml");
        } catch (IOException e) {
            logger.error("Scene configuration file not found. " + e.getMessage());
        }
    }

    @FXML
    private void openEnterButton() {
        Stage stage = (Stage) Enter.getScene().getWindow();
        try {
            openButton(stage, "/views/enter-id-collab-view.fxml");
        } catch (IOException e) {
            logger.error("Scene configuration file not found. " + e.getMessage());
        }
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HBox.setHgrow(imageHBox, Priority.ALWAYS);
    }
}
