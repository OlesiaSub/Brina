    package org.hse.brina.utils;
    
    import javafx.fxml.FXML;
    import javafx.scene.control.Button;
    import javafx.scene.control.TextArea;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.Priority;
    import javafx.scene.layout.VBox;
    
    public class AudioRecognitionController {
        public Button loadAudio;
        public Button speechRecognitionButton;
        public TextArea resultArea;
        public Button Paste;
        public HBox pasteHBox;
        public HBox textHBox;
    
        @FXML
        public void initialize(){
            HBox.setHgrow(pasteHBox, Priority.ALWAYS);
            HBox.setHgrow(resultArea, Priority.ALWAYS);
            VBox.setVgrow(resultArea, Priority.ALWAYS);
            HBox.setHgrow(textHBox, Priority.ALWAYS);
        }
    
    }
    
