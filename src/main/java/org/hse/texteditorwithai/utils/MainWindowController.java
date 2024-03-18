package org.hse.texteditorwithai.utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hse.texteditorwithai.Config;
import org.hse.texteditorwithai.Main;
import richtext.RichTextDemo;

import java.io.IOException;

/**
 * MainWindowController управляет главным окном приложения,
 * отображает документы, доступные пользователю.
 */
public class MainWindowController {
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

    private void openButton(Stage stage, String fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlView));
        Parent logInLoader = loader.load();
        Scene scene = new Scene(logInLoader, Config.getDefaultWidth(), Config.getDefaultHeight());
        stage.setScene(scene);
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
            openButton(stage, "/org/hse/texteditorwithai/views/collab-view.fxml");
        } catch (IOException e) {
            System.err.println("Scene configuration file not found. " + e.getMessage());
        }
    }

    @FXML
    private void openMyProdButton() {
        Stage stage = (Stage) my.getScene().getWindow();
        try {
            openButton(stage, "/org/hse/texteditorwithai/views/my-projects-view.fxml");
        } catch (IOException e) {
            System.err.println("Scene configuration file not found. " + e.getMessage());
        }
    }

    @FXML
    private void openEnterButton() {
        Stage stage = (Stage) Enter.getScene().getWindow();
        try {
            openButton(stage, "/org/hse/texteditorwithai/views/enter-id-collab-view.fxml");
        } catch (IOException e) {
            System.err.println("Scene configuration file not found. " + e.getMessage());
        }
    }
}
