/*
 * Created 2014 by Tomas Mikula.
 *
 * The author dedicates this file to the public domain.
 */

package richtext;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.rtf.RTFParser;
import org.apache.tika.sax.WriteOutContentHandler;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.*;
import org.hse.brina.Main;
import org.reactfx.SuspendableNo;
import org.reactfx.util.Either;
import org.reactfx.util.Tuple2;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static org.fxmisc.richtext.model.TwoDimensional.Bias.Backward;
import static org.fxmisc.richtext.model.TwoDimensional.Bias.Forward;

public class RichTextDemo extends Application {

    // the saved/loaded files and their format are arbitrary and may change across versions
    public static final String RTFX_FILE_EXTENSION = ".rtf";
    public final FoldableStyledArea area = new FoldableStyledArea();
    public final SuspendableNo updatingToolbar = new SuspendableNo();
    public Stage mainStage;

    {
        area.setWrapText(true);
        area.setStyleCodecs(ParStyle.CODEC, Codec.styledSegmentCodec(Codec.eitherCodec(Codec.STRING_CODEC, LinkedImage.codec()), TextStyle.CODEC));
        area.setParagraphGraphicFactory(new BulletFactory(area));  // and folded paragraph indicator
        area.setContextMenu(new DefaultContextMenu());
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        mainStage = primaryStage;

        Button loadBtn = createButton("loadfile", this::loadDocument, "Load document.\n\n" + "Note: the demo will load only previously-saved \"" + RTFX_FILE_EXTENSION + "\" files. " + "This file format is abitrary and may change across versions.");
        Button saveBtn = createButton("savefile", this::saveDocument, "Save document.\n\n" + "Note: the demo will save the area's content to a \"" + RTFX_FILE_EXTENSION + "\" file. " + "This file format is abitrary and may change across versions.");
        CheckBox wrapToggle = new CheckBox("Wrap");
        wrapToggle.setSelected(true);
        area.wrapTextProperty().bind(wrapToggle.selectedProperty());

        Button backBtn = new Button();
        backBtn.getStyleClass().add("back");
        backBtn.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/org/hse/brina/views/main-window-view.fxml"));
            Parent pageLoader = null;
            try {
                pageLoader = loader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Scene scene = new Scene(pageLoader, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(scene);
            area.requestFocus();
        });
        backBtn.setPrefWidth(25);
        backBtn.setPrefHeight(25);
        backBtn.setTooltip(new Tooltip("Back"));
        Image image = new Image("/richtext/document-icon.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);

        Button undoBtn = createButton("undo", area::undo, "Undo");
        Button redoBtn = createButton("redo", area::redo, "Redo");
        Button cutBtn = createButton("cut", area::cut, "Cut");
        Button copyBtn = createButton("copy", area::copy, "Copy");
        Button pasteBtn = createButton("paste", area::paste, "Paste");
        Button boldBtn = createButton("bold", this::toggleBold, "Bold");
        Button italicBtn = createButton("italic", this::toggleItalic, "Italic");
        Button underlineBtn = createButton("underline", this::toggleUnderline, "Underline");
        Button strikeBtn = createButton("strikethrough", this::toggleStrikethrough, "Strike Trough");
        Button insertImageBtn = createButton("insertimage", this::insertImage, "Insert Image");
        Button increaseIndentBtn = createButton("increaseIndent", this::increaseIndent, "Increase indent");
        Button decreaseIndentBtn = createButton("decreaseIndent", this::decreaseIndent, "Decrease indent");
        ToggleGroup alignmentGrp = new ToggleGroup();
        ToggleButton alignLeftBtn = createToggleButton(alignmentGrp, "align-left", this::alignLeft, "Align left");
        ToggleButton alignCenterBtn = createToggleButton(alignmentGrp, "align-center", this::alignCenter, "Align center");
        ToggleButton alignRightBtn = createToggleButton(alignmentGrp, "align-right", this::alignRight, "Align right");
        ToggleButton alignJustifyBtn = createToggleButton(alignmentGrp, "align-justify", this::alignJustify, "Justify");

        HBox HBoxParagraphBackground = new HBox();
        HBoxParagraphBackground.setStyle("-fx-background-color: white; -fx-alignment: center; -fx-spacing: 5px;");
        HBoxParagraphBackground.setPadding(new Insets(10));
        ColorPicker paragraphBackgroundPicker = new ColorPicker();
        paragraphBackgroundPicker.setStyle(" -fx-color-label-visible: false;");
        Text paragraphBackgroundText = new Text("Paragraph background");
        paragraphBackgroundText.setStyle("-fx-text-alignment: left; -fx-alignment: center-left; -fx-font-size: 12px");
        HBoxParagraphBackground.getChildren().addAll(paragraphBackgroundPicker, paragraphBackgroundText);

        ComboBox<Integer> sizeCombo = new ComboBox<>(FXCollections.observableArrayList(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 18, 20, 22, 24, 28, 32, 36, 40, 48, 56, 64, 72));
        sizeCombo.setStyle(" -fx-color-label-visible: false;");
        sizeCombo.getSelectionModel().select(Integer.valueOf(12));
        sizeCombo.setTooltip(new Tooltip("Font size"));
        ComboBox<String> familyCombo = new ComboBox<>(FXCollections.observableList(Font.getFamilies()));
        familyCombo.setStyle(" -fx-color-label-visible: false;");
        familyCombo.getSelectionModel().select("Serif");
        familyCombo.setTooltip(new Tooltip("Font family"));

        HBox HBoxText = new HBox();
        HBoxText.setStyle("-fx-background-color: white; -fx-alignment: center; -fx-spacing: 5px;");
        HBoxText.setPadding(new Insets(10));
        ColorPicker textColorPicker = new ColorPicker(Color.BLACK);
        textColorPicker.setStyle(" -fx-color-label-visible: false;");
        Text textColorText = new Text("Text");
        textColorText.setStyle("-fx-text-alignment: left; -fx-alignment: center-left;-fx-font-size: 12px");
        HBoxText.getChildren().addAll(textColorPicker, textColorText);

        HBox HBoxBackground = new HBox();
        HBoxBackground.setStyle("-fx-background-color: white; -fx-alignment: center; -fx-spacing: 5px;");
        HBoxBackground.setPadding(new Insets(10));
        ColorPicker backgroundColorPicker = new ColorPicker();
        backgroundColorPicker.setStyle(" -fx-color-label-visible: false;");
        Text backgroundColorText = new Text("Background");
        backgroundColorText.setStyle("-fx-text-alignment: left; -fx-alignment: center-left; -fx-font-size: 12px");
        HBoxBackground.getChildren().addAll(backgroundColorPicker, backgroundColorText);

        paragraphBackgroundPicker.setTooltip(new Tooltip("Paragraph background"));
        textColorPicker.setTooltip(new Tooltip("Text color"));
        backgroundColorPicker.setTooltip(new Tooltip("Text background"));

        paragraphBackgroundPicker.valueProperty().addListener((o, old, color) -> updateParagraphBackground(color));
        sizeCombo.setOnAction(evt -> updateFontSize(sizeCombo.getValue()));
        familyCombo.setOnAction(evt -> updateFontFamily(familyCombo.getValue()));
        textColorPicker.valueProperty().addListener((o, old, color) -> updateTextColor(color));
        backgroundColorPicker.valueProperty().addListener((o, old, color) -> updateBackgroundColor(color));

        undoBtn.disableProperty().bind(area.undoAvailableProperty().map(x -> !x));
        redoBtn.disableProperty().bind(area.redoAvailableProperty().map(x -> !x));

        BooleanBinding selectionEmpty = new BooleanBinding() {
            {
                bind(area.selectionProperty());
            }

            @Override
            protected boolean computeValue() {
                return area.getSelection().getLength() == 0;
            }
        };

        cutBtn.disableProperty().bind(selectionEmpty);
        copyBtn.disableProperty().bind(selectionEmpty);

        area.beingUpdatedProperty().addListener((o, old, beingUpdated) -> {
            if (!beingUpdated) {
                boolean bold, italic, underline, strike;
                Integer fontSize;
                String fontFamily;
                Color textColor;
                Color backgroundColor;

                IndexRange selection = area.getSelection();
                if (selection.getLength() != 0) {
                    StyleSpans<TextStyle> styles = area.getStyleSpans(selection);
                    bold = styles.styleStream().anyMatch(s -> s.bold.orElse(false));
                    italic = styles.styleStream().anyMatch(s -> s.italic.orElse(false));
                    underline = styles.styleStream().anyMatch(s -> s.underline.orElse(false));
                    strike = styles.styleStream().anyMatch(s -> s.strikethrough.orElse(false));
                    int[] sizes = styles.styleStream().mapToInt(s -> s.fontSize.orElse(-1)).distinct().toArray();
                    fontSize = sizes.length == 1 ? sizes[0] : -1;
                    String[] families = styles.styleStream().map(s -> s.fontFamily.orElse(null)).distinct().toArray(String[]::new);
                    fontFamily = families.length == 1 ? families[0] : null;
                    Color[] colors = styles.styleStream().map(s -> s.textColor.orElse(null)).distinct().toArray(Color[]::new);
                    textColor = colors.length == 1 ? colors[0] : null;
                    Color[] backgrounds = styles.styleStream().map(s -> s.backgroundColor.orElse(null)).distinct().toArray(i -> new Color[i]);
                    backgroundColor = backgrounds.length == 1 ? backgrounds[0] : null;
                } else {
                    int p = area.getCurrentParagraph();
                    int col = area.getCaretColumn();
                    TextStyle style = area.getStyleAtPosition(p, col);
                    bold = style.bold.orElse(false);
                    italic = style.italic.orElse(false);
                    underline = style.underline.orElse(false);
                    strike = style.strikethrough.orElse(false);
                    fontSize = style.fontSize.orElse(-1);
                    fontFamily = style.fontFamily.orElse(null);
                    textColor = style.textColor.orElse(null);
                    backgroundColor = style.backgroundColor.orElse(null);
                }

                int startPar = area.offsetToPosition(selection.getStart(), Forward).getMajor();
                int endPar = area.offsetToPosition(selection.getEnd(), Backward).getMajor();
                List<Paragraph<ParStyle, Either<String, LinkedImage>, TextStyle>> pars = area.getParagraphs().subList(startPar, endPar + 1);

                @SuppressWarnings("unchecked") Optional<TextAlignment>[] alignments = pars.stream().map(p -> p.getParagraphStyle().alignment).distinct().toArray(Optional[]::new);
                Optional<TextAlignment> alignment = alignments.length == 1 ? alignments[0] : Optional.empty();

                @SuppressWarnings("unchecked") Optional<Color>[] paragraphBackgrounds = pars.stream().map(p -> p.getParagraphStyle().backgroundColor).distinct().toArray(Optional[]::new);
                Optional<Color> paragraphBackground = paragraphBackgrounds.length == 1 ? paragraphBackgrounds[0] : Optional.empty();

                updatingToolbar.suspendWhile(() -> {
                    if (bold) {
                        if (!boldBtn.getStyleClass().contains("pressed")) {
                            boldBtn.getStyleClass().add("pressed");
                        }
                    } else {
                        boldBtn.getStyleClass().remove("pressed");
                    }

                    if (italic) {
                        if (!italicBtn.getStyleClass().contains("pressed")) {
                            italicBtn.getStyleClass().add("pressed");
                        }
                    } else {
                        italicBtn.getStyleClass().remove("pressed");
                    }

                    if (underline) {
                        if (!underlineBtn.getStyleClass().contains("pressed")) {
                            underlineBtn.getStyleClass().add("pressed");
                        }
                    } else {
                        underlineBtn.getStyleClass().remove("pressed");
                    }

                    if (strike) {
                        if (!strikeBtn.getStyleClass().contains("pressed")) {
                            strikeBtn.getStyleClass().add("pressed");
                        }
                    } else {
                        strikeBtn.getStyleClass().remove("pressed");
                    }

                    if (alignment.isPresent()) {
                        TextAlignment al = alignment.get();
                        switch (al) {
                            case LEFT:
                                alignmentGrp.selectToggle(alignLeftBtn);
                                break;
                            case CENTER:
                                alignmentGrp.selectToggle(alignCenterBtn);
                                break;
                            case RIGHT:
                                alignmentGrp.selectToggle(alignRightBtn);
                                break;
                            case JUSTIFY:
                                alignmentGrp.selectToggle(alignJustifyBtn);
                                break;
                        }
                    } else {
                        alignmentGrp.selectToggle(null);
                    }

                    paragraphBackgroundPicker.setValue(paragraphBackground.orElse(null));

                    if (fontSize != -1) {
                        sizeCombo.getSelectionModel().select(fontSize);
                    } else {
                        sizeCombo.getSelectionModel().clearSelection();
                    }

                    if (fontFamily != null) {
                        familyCombo.getSelectionModel().select(fontFamily);
                    } else {
                        familyCombo.getSelectionModel().clearSelection();
                    }

                    if (textColor != null) {
                        textColorPicker.setValue(textColor);
                    }

                    backgroundColorPicker.setValue(backgroundColor);
                });
            }
        });

        TextField documentName = new TextField();
        documentName.setText("New Document");
        documentName.setMaxWidth(200);
        documentName.setMaxHeight(50);
        documentName.setPrefWidth(100);
        documentName.setAlignment(Pos.BASELINE_LEFT);
        ToolBar toolBar1 = new ToolBar(backBtn, loadBtn, saveBtn, new Separator(Orientation.VERTICAL), undoBtn, redoBtn, new Separator(Orientation.VERTICAL), cutBtn, copyBtn, pasteBtn, new Separator(Orientation.VERTICAL), boldBtn, italicBtn, underlineBtn, strikeBtn, new Separator(Orientation.VERTICAL), alignLeftBtn, alignCenterBtn, alignRightBtn, alignJustifyBtn, new Separator(Orientation.VERTICAL), increaseIndentBtn, decreaseIndentBtn, new Separator(Orientation.VERTICAL), insertImageBtn, new Separator(Orientation.VERTICAL), HBoxParagraphBackground);

        String toolBarStyle = "-fx-background-color:white; -fx-padding: 0px;";
        toolBar1.setStyle(toolBarStyle);
        toolBar1.setPrefHeight(30);
        toolBar1.setMaxHeight(30);

        VBox upperToolbarVBox = new VBox();
        upperToolbarVBox.setStyle("-fx-background-color: white; -fx-alignment: TOP_LEFT; -fx-spacing: 1px;");
        upperToolbarVBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(upperToolbarVBox, Priority.ALWAYS);
        VBox.setVgrow(upperToolbarVBox, Priority.ALWAYS);
        upperToolbarVBox.getChildren().addAll(documentName, toolBar1);

        HBox upperToolbarHBox = new HBox();
        upperToolbarHBox.setStyle("-fx-background-color: white; -fx-alignment: TOP_LEFT; -fx-spacing: 0px;");
        upperToolbarHBox.setAlignment(Pos.TOP_LEFT);
        upperToolbarHBox.setPadding(new Insets(3));
        HBox.setHgrow(upperToolbarHBox, Priority.ALWAYS);
        upperToolbarHBox.getChildren().addAll(imageView, upperToolbarVBox);

        ToolBar toolBar2 = new ToolBar(sizeCombo, familyCombo, HBoxText, HBoxBackground);
        toolBar2.setStyle(toolBarStyle);
        toolBar2.setPrefHeight(30);
        toolBar2.setMaxHeight(30);

        VirtualizedScrollPane<GenericStyledArea<ParStyle, Either<String, LinkedImage>, TextStyle>> vsPane = new VirtualizedScrollPane<>(area);
        VBox windowVBox = new VBox();
        windowVBox.setStyle("-fx-background-color: white; -fx-alignment: TOP_LEFT; -fx-spacing: 1px;");
        windowVBox.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(vsPane, Priority.ALWAYS);

        windowVBox.getChildren().addAll(upperToolbarHBox, new Separator(Orientation.HORIZONTAL), toolBar2, new Separator(Orientation.HORIZONTAL), vsPane);

        Scene scene = new Scene(windowVBox, primaryStage.getWidth(), primaryStage.getHeight());

        if (RichTextDemo.class.getResource("rich-text.css") != null) {
            scene.getStylesheets().add(RichTextDemo.class.getResource("rich-text.css").toExternalForm());
        } else {
            System.out.println("rich-text.css not found!");
        }

        primaryStage.setScene(scene);
        area.requestFocus();
        primaryStage.show();
    }

    public Button createButton(String styleClass, Runnable action, String toolTip) {
        Button button = new Button();
        button.getStyleClass().add(styleClass);
        button.setOnAction(evt -> {
            action.run();
            area.requestFocus();
        });
        button.setPrefWidth(25);
        button.setPrefHeight(25);
        if (toolTip != null) {
            button.setTooltip(new Tooltip(toolTip));
        }
        return button;
    }

    public ToggleButton createToggleButton(ToggleGroup grp, String styleClass, Runnable action, String toolTip) {
        ToggleButton button = new ToggleButton();
        button.setToggleGroup(grp);
        button.getStyleClass().add(styleClass);
        button.setOnAction(evt -> {
            action.run();
            area.requestFocus();
        });
        button.setPrefWidth(25);
        button.setPrefHeight(25);
        if (toolTip != null) {
            button.setTooltip(new Tooltip(toolTip));
        }
        return button;
    }

    public void toggleBold() {
        updateStyleInSelection(spans -> TextStyle.bold(!spans.styleStream().allMatch(style -> style.bold.orElse(false))));
    }

    public void toggleItalic() {
        updateStyleInSelection(spans -> TextStyle.italic(!spans.styleStream().allMatch(style -> style.italic.orElse(false))));
    }

    public void toggleUnderline() {
        updateStyleInSelection(spans -> TextStyle.underline(!spans.styleStream().allMatch(style -> style.underline.orElse(false))));
    }

    public void toggleStrikethrough() {
        updateStyleInSelection(spans -> TextStyle.strikethrough(!spans.styleStream().allMatch(style -> style.strikethrough.orElse(false))));
    }

    public void alignLeft() {
        updateParagraphStyleInSelection(ParStyle.alignLeft());
    }

    public void alignCenter() {
        updateParagraphStyleInSelection(ParStyle.alignCenter());
    }

    public void alignRight() {
        updateParagraphStyleInSelection(ParStyle.alignRight());
    }

    public void alignJustify() {
        updateParagraphStyleInSelection(ParStyle.alignJustify());
    }

    public void loadDocument() {
        String initialDir = System.getProperty("user.dir");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load document");
        fileChooser.setInitialDirectory(new File(initialDir));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Arbitrary RTFX file", "*" + RTFX_FILE_EXTENSION));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            area.clear();
            load(selectedFile);
        }
    }

    public void load(File file) {
        if (area.getStyleCodecs().isPresent()) {
            Tuple2<Codec<ParStyle>, Codec<StyledSegment<Either<String, LinkedImage>, TextStyle>>> codecs = area.getStyleCodecs().get();
            Codec<StyledDocument<ParStyle, Either<String, LinkedImage>, TextStyle>> codec = ReadOnlyStyledDocument.codec(codecs._1, codecs._2, area.getSegOps());

            try {
                FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis);
                StyledDocument<ParStyle, Either<String, LinkedImage>, TextStyle> doc = codec.decode(dis);
                fis.close();

                if (doc != null) {
                    area.replaceSelection(doc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDocument() {
        String initialDir = System.getProperty("user.dir");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save document");
        fileChooser.setInitialDirectory(new File(initialDir));
        fileChooser.setInitialFileName("example rtfx file" + RTFX_FILE_EXTENSION);
        File selectedFile = fileChooser.showSaveDialog(mainStage);
        if (selectedFile != null) {
            save(selectedFile);
        }
    }

    public void save(File file) {
        StyledDocument<ParStyle, Either<String, LinkedImage>, TextStyle> doc = area.getDocument();

        // Use the Codec to save the document in a binary format
        area.getStyleCodecs().ifPresent(codecs -> {
            Codec<StyledDocument<ParStyle, Either<String, LinkedImage>, TextStyle>> codec = ReadOnlyStyledDocument.codec(codecs._1, codecs._2, area.getSegOps());
            try {
                FileOutputStream fos = new FileOutputStream(file);
                DataOutputStream dos = new DataOutputStream(fos);
                codec.encode(dos, doc);
                fos.close();
//                covertRTFtoTXT(file);
//                RTFToTXTConverter.convertRtfToTxt(file.getAbsolutePath(), "C:\\Users\\HUAWEI\\Downloads");
            } catch (IOException fnfe) {
                fnfe.printStackTrace();
            }
        });
    }

    public void covertRTFtoTXT(File rtfFilePath) {
//        String rtfFilePath = "sometext.rtf";
        String txtFilePath = "sometexttxt.txt";

        try {
            // Создание парсера RTF
            RTFParser rtfParser = new RTFParser();

            // Поток ввода для чтения RTF-файла
            InputStream inputStream = new FileInputStream(rtfFilePath);

            // Поток вывода для записи текста в новый TXT-файл
            OutputStream outputStream = new FileOutputStream(txtFilePath);

            // Обработчик, который записывает текст в выходной поток
            WriteOutContentHandler handler = new WriteOutContentHandler(outputStream);

            // Преобразование RTF в текст с использованием Apache Tika
            rtfParser.parse(inputStream, handler, new Metadata(), new ParseContext());

            // Закрытие потоков
            inputStream.close();
            outputStream.close();

            System.out.println("Файл успешно сохранен в формате TXT.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TikaException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        } catch (SAXException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
    }

    /**
     * Action listener which inserts a new image at the current caret position.
     */
    public void insertImage() {
        String initialDir = System.getProperty("user.dir");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Insert image");
        fileChooser.setInitialDirectory(new File(initialDir));
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            imagePath = imagePath.replace('\\', '/');
            ReadOnlyStyledDocument<ParStyle, Either<String, LinkedImage>, TextStyle> ros = ReadOnlyStyledDocument.fromSegment(Either.right(new RealLinkedImage(imagePath)), ParStyle.EMPTY, TextStyle.EMPTY, area.getSegOps());
            area.replaceSelection(ros);
        }
    }

    public void increaseIndent() {
        updateParagraphStyleInSelection(ps -> ps.increaseIndent());
    }

    public void decreaseIndent() {
        updateParagraphStyleInSelection(ps -> ps.decreaseIndent());
    }

    public void updateStyleInSelection(Function<StyleSpans<TextStyle>, TextStyle> mixinGetter) {
        IndexRange selection = area.getSelection();
        if (selection.getLength() != 0) {
            StyleSpans<TextStyle> styles = area.getStyleSpans(selection);
            TextStyle mixin = mixinGetter.apply(styles);
            StyleSpans<TextStyle> newStyles = styles.mapStyles(style -> style.updateWith(mixin));
            area.setStyleSpans(selection.getStart(), newStyles);
        }
    }

    public void updateStyleInSelection(TextStyle mixin) {
        IndexRange selection = area.getSelection();
        if (selection.getLength() != 0) {
            StyleSpans<TextStyle> styles = area.getStyleSpans(selection);
            StyleSpans<TextStyle> newStyles = styles.mapStyles(style -> style.updateWith(mixin));
            area.setStyleSpans(selection.getStart(), newStyles);
        }
    }

    public void updateParagraphStyleInSelection(Function<ParStyle, ParStyle> updater) {
        IndexRange selection = area.getSelection();
        int startPar = area.offsetToPosition(selection.getStart(), Forward).getMajor();
        int endPar = area.offsetToPosition(selection.getEnd(), Backward).getMajor();
        for (int i = startPar; i <= endPar; ++i) {
            Paragraph<ParStyle, Either<String, LinkedImage>, TextStyle> paragraph = area.getParagraph(i);
            area.setParagraphStyle(i, updater.apply(paragraph.getParagraphStyle()));
        }
    }

    public void updateParagraphStyleInSelection(ParStyle mixin) {
        updateParagraphStyleInSelection(style -> style.updateWith(mixin));
    }

    public void updateFontSize(Integer size) {
        if (!updatingToolbar.get()) {
            updateStyleInSelection(TextStyle.fontSize(size));
        }
    }

    public void updateFontFamily(String family) {
        if (!updatingToolbar.get()) {
            updateStyleInSelection(TextStyle.fontFamily(family));
        }
    }

    public void updateTextColor(Color color) {
        if (!updatingToolbar.get()) {
            updateStyleInSelection(TextStyle.textColor(color));
        }
    }

    public void updateBackgroundColor(Color color) {
        if (!updatingToolbar.get()) {
            updateStyleInSelection(TextStyle.backgroundColor(color));
        }
    }

    public void updateParagraphBackground(Color color) {
        if (!updatingToolbar.get()) {
            updateParagraphStyleInSelection(ParStyle.backgroundColor(color));
        }
    }

    static class FoldableStyledArea extends GenericStyledArea<ParStyle, Either<String, LinkedImage>, TextStyle> {
        public final static TextOps<String, TextStyle> styledTextOps = SegmentOps.styledTextOps();
        public final static LinkedImageOps<TextStyle> linkedImageOps = new LinkedImageOps<>();

        public FoldableStyledArea() {
            super(ParStyle.EMPTY,                                                 // default paragraph style
                    (paragraph, style) -> paragraph.setStyle(style.toCss()),        // paragraph style setter
                    TextStyle.EMPTY.updateFontSize(12).updateFontFamily("Serif").updateTextColor(Color.BLACK),  // default segment style
                    styledTextOps._or(linkedImageOps, (s1, s2) -> Optional.empty()),                            // segment operations
                    seg -> createNode(seg, (text, style) -> text.setStyle(style.toCss())));                     // Node creator and segment style setter
        }

        public static Node createNode(StyledSegment<Either<String, LinkedImage>, TextStyle> seg, BiConsumer<? super TextExt, TextStyle> applyStyle) {
            return seg.getSegment().unify(text -> StyledTextArea.createStyledTextNode(text, seg.getStyle(), applyStyle), LinkedImage::createNode);
        }

        public void foldParagraphs(int startPar, int endPar) {
            foldParagraphs(startPar, endPar, getAddFoldStyle());
        }

        public void foldSelectedParagraphs() {
            foldSelectedParagraphs(getAddFoldStyle());
        }

        public void foldText(int start, int end) {
            fold(start, end, getAddFoldStyle());
        }

        public void unfoldParagraphs(int startingFromPar) {
            unfoldParagraphs(startingFromPar, getFoldStyleCheck(), getRemoveFoldStyle());
        }

        public void unfoldText(int startingFromPos) {
            startingFromPos = offsetToPosition(startingFromPos, Bias.Backward).getMajor();
            unfoldParagraphs(startingFromPos, getFoldStyleCheck(), getRemoveFoldStyle());
        }

        protected UnaryOperator<ParStyle> getAddFoldStyle() {
            return pstyle -> pstyle.updateFold(true);
        }

        protected UnaryOperator<ParStyle> getRemoveFoldStyle() {
            return pstyle -> pstyle.updateFold(false);
        }

        protected Predicate<ParStyle> getFoldStyleCheck() {
            return pstyle -> pstyle.isFolded();
        }
    }

    public class DefaultContextMenu extends ContextMenu {
        public MenuItem fold, unfold;

        public DefaultContextMenu() {
            fold = new MenuItem("Fold selected text");
            fold.setOnAction(AE -> {
                hide();
                fold();
            });

            unfold = new MenuItem("Unfold from cursor");
            unfold.setOnAction(AE -> {
                hide();
                unfold();
            });

            getItems().addAll(fold, unfold);
        }

        /**
         * Folds multiple lines of selected text, only showing the first line and hiding the rest.
         */
        public void fold() {
            ((FoldableStyledArea) getOwnerNode()).foldSelectedParagraphs();
        }

        /**
         * Unfold the CURRENT line/paragraph if it has a fold.
         */
        public void unfold() {
            FoldableStyledArea area = (FoldableStyledArea) getOwnerNode();
            area.unfoldParagraphs(area.getCurrentParagraph());
        }
    }
}
