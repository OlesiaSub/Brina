package org.demo.texteditorwithai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static final int defaultWidth = 600;
    public static final int defaultHeight = 400;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Parent root = loader.load();
        stage.setTitle("Text Editor with AI");

        /*TODO: set an app icon
        InputStream iconStream = getClass().getResourceAsStream("/icon.png");
        Image image = new Image(iconStream);
        primaryStage.getIcons().add(image);
         */

        Scene scene = new Scene(root, defaultWidth, defaultHeight);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}