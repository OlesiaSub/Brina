package org.hse.brina;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Main является точкой входа в приложение и запускает его,
 * загружает настройки из файла "config.properties", устанавливает иконку приложения,
 * создает сцену и запускает основное окно приложения.
 */
public class Main extends Application {

    private static final Logger logger = LogManager.getLogger();


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/first-view.fxml"));
        Parent root = loader.load();
        stage.setTitle("Brina");
        try (InputStream iconStream1 = getClass().getResourceAsStream("assets/small-icon.png")) {
            Image icon = new Image(iconStream1);
            stage.getIcons().add(icon);
        } catch (Exception e) {
            logger.error("Error loading icon: " + e.getMessage());
        }
        Scene scene = new Scene(root, Config.getDefaultWidth(), Config.getDefaultHeight());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}