package org.hse.brina.utils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DocumentListItemController implements Initializable {
    @FXML
    public ImageView statusImage;
    @FXML
    public Text documentName;
    @FXML
    public HBox nameHBox;
    @FXML
    public HBox globalHBox;

    public void setData(Document document){
        documentName.setText(document.getName());
//        statusImage.setImage(new Image(String.valueOf(getClass().getResourceAsStream(document.getImage()))));
        HBox.setHgrow(nameHBox, Priority.ALWAYS);
        HBox.setHgrow(globalHBox, Priority.ALWAYS);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
