package org.hse.brina.utils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.hse.brina.richtext.RichTextDemo;

public class ChatGptController extends RichTextDemo{
    public MenuBar chatOptions;
    @FXML
    public Button goButton;
    @FXML
    public Button okButton;
    @FXML
    public TextArea textArea;
    @FXML
    public HBox menuHBox;
    public MenuItem option1;
    public MenuItem option2;

    StringBuilder selectedOption = new StringBuilder();

    @FXML
    public void initialize(){
        HBox.setHgrow(menuHBox, Priority.ALWAYS);
        HBox.setHgrow(textArea, Priority.ALWAYS);
        VBox.setVgrow(textArea, Priority.ALWAYS);
//        option1.addEventHandler(ActionEvent event);

    }

    @FXML
    public void item1Selected(){
        selectedOption.replace(0, selectedOption.length(), "first");
    }
    @FXML
    public void item2Selected(){
        selectedOption.replace(0, selectedOption.length(), "second");
    }

    @FXML
    public void sendButtonClicked(){
        //chatGPT request with text and request option --> docArea.getText(); selectedOption.toString()
        String text = docArea.getText();
//        System.out.println(text);
    }

    @FXML
    public void okButtonClicked(){
        //TODO: change the text of the RichText with needed style
    }
}
