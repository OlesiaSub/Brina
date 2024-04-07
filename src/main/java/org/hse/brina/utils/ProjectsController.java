package org.hse.brina.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hse.brina.Config;
import org.hse.brina.Main;
import org.hse.brina.richtext.RichTextDemo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProjectsController {
    @FXML
    public ListView<String> documentList;
    @FXML
    public Button backButton;
    private Map<String, String> userDocuments;
    private static final Logger logger = LogManager.getLogger();

    public void initialize() {
        String username = Config.client.getName();
        Config.client.sendMessage("getDocuments " + username);
        String response = Config.client.receiveMessage();

        userDocuments = new LinkedHashMap<>();
        String[] pairs = response.split(" ");
        for (int i = 0; i < pairs.length - 1; i += 2) {
            userDocuments.put(pairs[i], pairs[i + 1]);
        }
        documentList.getItems().addAll(userDocuments.keySet());
    }

    @FXML
    public void documentClicked(MouseEvent event) {
        Stage stage = (Stage) documentList.getScene().getWindow();
        String key = documentList.getSelectionModel().getSelectedItem();
        String value = userDocuments.get(key);
        RichTextDemo richTextWindow = new RichTextDemo();
        richTextWindow.previousView = "/org/hse/brina/views/projects-view.fxml";
        richTextWindow.start(stage);
        File file = new File(value);
        if(file.exists()) richTextWindow.load(new File(value)); //если это файл пользователя
        //если это файл другого пользователя, то ввод ключа на совместное редактирование
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        try {
            loadScene(stage, "/org/hse/brina/views/main-window-view.fxml");
        } catch (IOException e) {
            logger.error("Scene configuration file not found. " + e.getMessage());
        }
    }
    private void loadScene(Stage stage, String fxmlView) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlView));
        Parent logInLoader = loader.load();
        Scene scene = new Scene(logInLoader, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }
}
