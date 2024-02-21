module org.demo.texteditorwithai {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;


    opens org.demo.texteditorwithai to javafx.fxml;
    exports org.demo.texteditorwithai;
}