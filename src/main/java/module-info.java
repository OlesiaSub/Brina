module org.hse.texteditorwithai {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;


    opens org.hse.texteditorwithai to javafx.fxml;
    exports org.hse.texteditorwithai;
    exports org.hse.texteditorwithai.signing;
    opens org.hse.texteditorwithai.signing to javafx.fxml;
    exports org.hse.texteditorwithai.utils;
    opens org.hse.texteditorwithai.utils to javafx.fxml;
}