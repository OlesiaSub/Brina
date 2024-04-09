package org.hse.brina.utils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Objects;
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
    @FXML
    public Text accessText;

    public void setData(Document document) {
        documentName.setText(document.getName());
        HBox.setHgrow(nameHBox, Priority.ALWAYS);
        HBox.setHgrow(globalHBox, Priority.ALWAYS);
        document.setStatus("unlocked");
        if (document.getAccess().equals("r")) {
            accessText.setText("reader");
            Image lockedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/hse/brina/assets/locked.png")));
            statusImage.setImage(lockedImage);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
