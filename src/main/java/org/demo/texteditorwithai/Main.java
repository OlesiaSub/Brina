package org.demo.texteditorwithai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    public static final int defaultWidth = 600;
    public static final int defaultHeight = 400;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("first-view.fxml"));
        Parent root = loader.load();
        stage.setTitle("Brina");

        InputStream iconStream = getClass().getResourceAsStream("small-icon.png");
        Image icon = null;
        if (iconStream != null) {
            icon = new Image(iconStream);
        } else {
            System.err.println("Icon is not Found!");
        }
        stage.getIcons().add(icon);
        Scene scene = new Scene(root, defaultWidth, defaultHeight);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}