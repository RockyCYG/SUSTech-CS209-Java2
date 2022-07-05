package com.example.assignment2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javafx.scene.control.SelectionMode.SINGLE;


public class TextReaderController {
    @FXML
    private VBox vBoxPane;

    @FXML
    private ListView<FileCell> fileListView;

    @FXML
    private WebView webView;

    @FXML
    private Label filePathLabel;

    @FXML
    private Label wordsNumberLabel;

    private String defaultStyle;

    private String markStyle;

    /**
     * The method fileListOnDragDropped(DragEvent event) is the callback function
     * when the mouse drag a file (files) to the ListView component and then drop.
     *
     * @param event: DragEvent object
     */
    @FXML
    public void fileListOnDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        ObservableList<FileCell> items = fileListView.getItems();
        if (dragboard.hasFiles()) {
            List<File> files = dragboard.getFiles();
            for (File file : files) {
                boolean flag = false;
                for (FileCell fileCell : fileListView.getItems()) {
                    if (fileCell.getFile().equals(file)) {
                        fileListView.getSelectionModel().select(fileCell);
                        fileListItemOnSelected();
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    items.add(new FileCell(file));
                }
            }
        }
    }

    /**
     * The method fileListOnDragOver(DragEvent event) is the callback function
     * when the mouse drag a file (files) over the ListView Component.
     *
     * @param event: DragEvent object
     */
    @FXML
    public void fileListOnDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
    }

    /**
     * The method openFile() is to open a file using FileChooser API,
     * then add the file object to the fileListView Component.
     * <p>
     * You should restrict the fileChooser to only select *.txt files.
     * Hint: FileChooser.ExtensionFilter()
     * <p>
     * Useful FileChooser API Refer:
     * https://www.w3cschool.cn/java/javafx-filechooser.html
     */
    @FXML
    public void openFile() {
        System.out.println("openFile() method has been triggered! ");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            for (FileCell fileCell : fileListView.getItems()) {
                if (fileCell.getFile().equals(file)) {
                    fileListView.getSelectionModel().select(fileCell);
                    fileListItemOnSelected();
                    return;
                }
            }
            fileListView.getItems().add(new FileCell(file));
        }
    }

    /**
     * The method saveFile() is to save the selected file to the disk.
     * 1. The "selected file" is the selected item in fileListView.
     * 2. If selected file is null, just do nothing,
     * else, save the textContent in the reader to disk.
     * <p>
     * Hint:
     * The selected item is a FileCell object, which encapsulates the File object (getFile()).
     */
    @FXML
    public void saveFile() {
        System.out.println("saveFile() method has been triggered! ");
        MultipleSelectionModel<FileCell> selectionModel = fileListView.getSelectionModel();
        FileCell fileCell = selectionModel.getSelectedItem();
        if (fileCell == null) {
            return;
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileCell.getFile()))) {
            bufferedWriter.write(fileCell.getFileContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matcher m = Pattern.compile("\\b[\\w'-]+\\b").matcher(fileCell.getFileContent());
        int num = 0;
        while (m.find()) {
            num++;
        }
        fileCell.setWordsNumber(num);

    }

    /**
     * The method saveAsFile() is to save the selected file to another path in the disk.
     * 1. The "selected file" is the selected item in fileListView.
     * 2. If selected file is null, just do nothing,
     * else, use FileChooser to choose the new path to save.
     * <p>
     * You should restrict the fileChooser to only save *.txt files.
     * Hint: FileChooser.ExtensionFilter()
     * <p>
     * Attention:
     * When you have saved the file successfully,
     * the old file name in fileListView for this file should be changed to the new file name,
     * and update the attributes in the FileCell object.
     * <p>
     * Hint:
     * After updating the attributes in the FileCell object,
     * call fileListView.refresh() to refresh the UI Component.
     */
    @FXML
    public void saveAsFile() {
        System.out.println("saveAsFile() method has been triggered! ");
        MultipleSelectionModel<FileCell> selectionModel = fileListView.getSelectionModel();
        FileCell fileCell = selectionModel.getSelectedItem();
        if (fileCell == null) {
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File To");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            return;
        }
        if (!file.getPath().endsWith(".txt")) {
            file = new File(file.getPath() + ".txt");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(fileCell.getFileContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matcher m = Pattern.compile("\\b[\\w'-]+\\b").matcher(fileCell.getFileContent());
        int num = 0;
        while (m.find()) {
            num++;
        }
        fileCell.setWordsNumber(num);
        fileCell.setFile(file);
        fileCell.setFileName(file.getName());
        fileCell.setAbsoluteFilePath(file.getAbsolutePath());
        filePathLabel.setText(file.getAbsolutePath());
        fileListView.refresh();
    }

    /**
     * The method closeFile() is to close the selected file.
     * 1. The "selected file" is the selected item in fileListView.
     * 2. If selected file is null, just do nothing,
     * else, use Alert to ask user whether to save the file.
     * 1. If yes, save the file.
     * 2. If no, do nothing.
     * 3. Then, remove the item in fileListView, clear the selection, and clear the reader content.
     * <p>
     * Don't forget to refresh the fileListView.
     */
    @FXML
    public void closeFile() {
        System.out.println("closeFile() method has been triggered! ");
        MultipleSelectionModel<FileCell> selectionModel = fileListView.getSelectionModel();
        FileCell fileCell = selectionModel.getSelectedItem();
        if (fileCell == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Confirmation");
        alert.setContentText("Save current file?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            saveFile();
        }
        fileListView.getItems().remove(fileCell);
        setReaderContent("");
        selectionModel.clearSelection();
        fileListView.refresh();
    }

    @FXML
    public void quit() {
        System.exit(0);
    }

    /**
     * The method updateWordsNumber() is to update the words number in bottom-right label.
     * 1. Statistic the words number for the textContent in reader.
     * 2. Set the words number in format "Words number: 123" for wordsNumberLabel object.
     */
    @FXML
    public void updateWordsNumber() {
        System.out.println("updateWordsNumber() method has been triggered! ");
        FileCell fileCell = fileListView.getSelectionModel().getSelectedItem();
        if (fileCell == null) {
            return;
        }
        String fileContent = fileCell.getFileContent();
        Matcher matcher = Pattern.compile("\\b[\\w'-]+\\b").matcher(fileContent);
        int num = 0;
        while (matcher.find()) {
            num++;
        }
        fileCell.setWordsNumber(num);
        wordsNumberLabel.setText("Words number: " + num);
    }

    /**
     * The method find() is to find the user-input word in the reader.
     * 1. Use TextInputDialog API for user to input a word.
     * 2. Highlight the word in the reader.
     * <p>
     * Hint:
     * To highlight a word in the reader, for simplicity,
     * you can just replace the word with String
     * "<span class=\"mark\">" + word + "</span>",
     * then set the replaced string to the reader.
     * <p>
     * Example:
     * To highlight "is" in "He is boy.",
     * "He <span class="mark">is</span> boy." is enough.
     * <p>
     * TextInputDialog API Refer:
     * https://blog.csdn.net/qq_26954773/article/details/78215554
     */
    @FXML
    public void find() {
        System.out.println("find() method has been triggered! ");
        MultipleSelectionModel<FileCell> selectionModel = fileListView.getSelectionModel();
        FileCell fileCell = selectionModel.getSelectedItem();
        if (fileCell == null) {
            return;
        }
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("find");
        textInputDialog.setContentText("Please enter a word which you want to search:");
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }
        if ("".equals(result.get())) {
            return;
        }
        String target = result.get();
        String fileContent = fileCell.getFileContent();
        String regex = "\\b(?i)" + Pattern.quote(target) + "\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileContent);
        int cnt = 0;
        while (matcher.find()) {
            String word = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            start += cnt * 26;
            end += cnt * 26;
            fileContent = fileContent.substring(0, start) + "<span class=\"mark\">" + word + "</span>"
                    + fileContent.substring(end);
            cnt++;
        }
        setReaderContent(fileContent);
    }

    /**
     * The method replaceAll() is to replaceAll the user-input string in the reader and replace it.
     * 1. Use TextInputDialog API for user to input the old string and the new string.
     * 2. Replace the old string with the new string in the reader.
     * 3. Highlight the replaced string in the reader.
     * 4. Then update the word number label.
     */
    @FXML
    public void replaceAll() {
        System.out.println("replaceAll() method has been triggered! ");
        MultipleSelectionModel<FileCell> selectionModel = fileListView.getSelectionModel();
        FileCell fileCell = selectionModel.getSelectedItem();
        if (fileCell == null) {
            return;
        }
        TextInputDialog textInputDialog1 = new TextInputDialog();
        textInputDialog1.setHeaderText("replaceAll");
        textInputDialog1.setContentText("Please enter a word you want to replace:");
        Optional<String> result1 = textInputDialog1.showAndWait();
        if (result1.isEmpty() || "".equals(result1.get())) {
            return;
        }
        String originWord = result1.get();
        originWord = originWord.toLowerCase();
        TextInputDialog textInputDialog2 = new TextInputDialog();
        textInputDialog2.setHeaderText("replaceAll");
        textInputDialog2.setContentText("Please enter what you want to replace to:");
        Optional<String> result2 = textInputDialog2.showAndWait();
        String newWord = result2.get();
        String fileContent = fileCell.getFileContent();
        String regex = "\\b(?i)" + Pattern.quote(originWord) + "\\b";
        String webContent = fileContent.replaceAll(regex, "<span class=\"mark\">" + newWord + "</span>");
        fileContent = fileContent.replaceAll(regex, newWord);
        setReaderContent(webContent);
        fileCell.setFileContent(fileContent);
        updateWordsNumber();
        fileListView.refresh();
    }

    /**
     * The method checkGrammar() is to check the correctness of the first letter of the English sentence.
     * 1. Find all the incorrect sentences with lower case letter as the first letter.
     * 2. Highlight the whole sentence in the reader.
     */
    @FXML
    public void checkGrammar() {
        System.out.println("checkGrammar() method has been triggered! ");
        FileCell fileCell = fileListView.getSelectionModel().getSelectedItem();
        if (fileCell == null) {
            return;
        }
        String fileContent = fileCell.getFileContent();
        String regex = "\\w[\\w-',:\"$\\s]*[.?!]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileContent);
        int cnt = 0;
        while (matcher.find()) {
            String sentence = matcher.group();
            if (Character.isUpperCase(sentence.charAt(0)) || Character.isDigit(sentence.charAt(0))) {
                continue;
            }
            int start = matcher.start();
            int end = matcher.end();
            start += cnt * 26;
            end += cnt * 26;
            fileContent = fileContent.substring(0, start) + "<span class=\"mark\">" + sentence + "</span>" + fileContent.substring(end);
            cnt++;
        }
        setReaderContent(fileContent);
    }

    /**
     * The method fixGrammar() is to fix the correctness of the first letter of the English sentence.
     * 1. Find all the incorrect sentences with lower case letter as the first letter.
     * 2. Fix the first letter of them.
     * 3. Highlight the whole fixed sentence in the reader.
     */
    @FXML
    public void fixGrammar() {
        System.out.println("fixGrammar() method has been triggered! ");
        FileCell fileCell = fileListView.getSelectionModel().getSelectedItem();
        if (fileCell == null) {
            return;
        }
        String fileContent = fileCell.getFileContent();
        String regex = "\\w[\\w-',:\"$\\s]*[.?!]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileContent);
        String webContent = fileContent;
        int cnt = 0;
        while (matcher.find()) {
            String sentence = matcher.group();
            if (Character.isUpperCase(sentence.charAt(0)) || Character.isDigit(sentence.charAt(0))) {
                continue;
            }
            int start = matcher.start();
            int end = matcher.end();
            fileContent = fileContent.substring(0, start) + Character.toUpperCase(sentence.charAt(0))
                    + fileContent.substring(start + 1);
            start += cnt * 26;
            end += cnt * 26;
            webContent = webContent.substring(0, start) + "<span class=\"mark\">" + Character.toUpperCase(sentence.charAt(0))
                    + sentence.substring(1) + "</span>" + webContent.substring(end);
            cnt++;
        }
        setReaderContent(webContent);
        fileCell.setFileContent(fileContent);
    }

    /**
     * The method statisticWordFrequency() is to statistic the top-10 words frequency in the reader.
     * <p>
     * Requirements:
     * 1. Use BarChart.
     * 2. The chart should have title, x-label, y-label and category for the top-10 words.
     * 3. The chart should be in order from left to right (in descending).
     * 4. The chart should be in a new stage.
     * <p>
     * Hint:
     * If there are not 10 different words in the reader, just top-n (n<10) is also ok.
     */
    @FXML
    public void statisticWordFrequency() {
        System.out.println("statisticWordFrequency() method has been triggered! ");
        FileCell fileCell = fileListView.getSelectionModel().getSelectedItem();
        if (fileCell == null) {
            return;
        }
        String fileContent = fileCell.getFileContent();
        String regex = "\\b[\\w'-]+\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileContent);
        Map<String, Integer> map = new TreeMap<>();
        while (matcher.find()) {
            String word = matcher.group();
            word = word.toLowerCase();
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1);
            }
        }
        CategoryAxis xAxis = new CategoryAxis();
        LinkedHashMap<String, Integer> newMap = map.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed()).limit(10).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> newVal, LinkedHashMap::new));
        xAxis.setCategories(FXCollections.observableArrayList(newMap.keySet()));
        xAxis.setLabel("Word");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frequency");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Top 10 Word Frequency Bar Chart");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : newMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(series);
        Group root = new Group(barChart);
        Scene scene = new Scene(root, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The method clearMark() is to clear the mark (highlight) in the reader.
     */
    @FXML
    public void clearMark() {
        System.out.println("clearMark() method has been triggered! ");
        setReaderContent(getReaderContent());
    }

    /**
     * The method setMarkStyle() is to prompt user to input a mark style string with format ".mark{...;...;}"
     * and set the new mark style to the reader.
     */
    @FXML
    public void setMarkStyle() {
        TextInputDialog dialog = new TextInputDialog("Set the mark style in css format");
        dialog.setHeaderText("Input the mark style in css format (.mark{...;...;})");
        Optional<String> result = dialog.showAndWait();
        this.markStyle = "";
        result.ifPresent(style -> {
            this.markStyle = style;
        });
        reload();
    }

    /**
     * The method setDefaultStyle() is to prompt user to input a default style string with format ".default{...;...;}"
     * and set the new default style to the reader.
     */
    @FXML
    public void setDefaultStyle() {
        TextInputDialog dialog = new TextInputDialog("Set the default style in css format");
        dialog.setHeaderText("Input the default style in css format (.default{...;...;})");
        Optional<String> result = dialog.showAndWait();
        this.defaultStyle = "";
        result.ifPresent(style -> {
            this.defaultStyle = style;
        });
        reload();
    }


    /**
     * The method fileListItemOnSelected() is the callback function when user select a item in ListView Component.
     */
    public void fileListItemOnSelected() {
        FileCell selectedItem = fileListView.getSelectionModel().getSelectedItem();
        setReaderContent(selectedItem.getFileContent());
        wordsNumberLabel.setText("Words number: " + selectedItem.getWordsNumber());
        filePathLabel.setText(selectedItem.getAbsoluteFilePath());
    }

    /**
     * The method preprocessHTMLDocument(String txtDocument) is to wrap the content string with
     * the standard HTML labels (such as <html></html>, <style></style>, etc.), to get the standard HTML document string.
     *
     * @param txtDocument: the content for reader (perhaps include some mark labels)
     * @return The html document string
     */
    public String preprocessHTMLDocument(String txtDocument) {
        return "<html>" +
                "<head>" + "<style>" + defaultStyle + markStyle + "</style>" + "</head>" +
                "<body contenteditable=\"false\">" +
                "<span class=\"default\">" + txtDocument.trim().replaceAll("\n", "<br>") + "</span>" +
                "</body>" +
                "</html>";
    }

    /**
     * The method postprocessHTMLDocument(String HTMLDocument) is to parse the html document string,
     * filtrate the html mark labels, return the pure content.
     *
     * @param HTMLDocument: the html document string (which includes <html></html>, <style></style>, etc.)
     * @return The content without any html mark labels
     */
    public String postprocessHTMLDocument(String HTMLDocument) {
        String postprocessDocument = HTMLDocument.replaceAll("<br>", "\n");

        String regExStyle = "]*?>[\\s\\S]*?</style>";
        Pattern patternStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
        Matcher matcherStyle = patternStyle.matcher(postprocessDocument);
        postprocessDocument = matcherStyle.replaceAll("");

        String regExHTML = "<[^>]+>";
        Pattern patternHTML = Pattern.compile(regExHTML, Pattern.CASE_INSENSITIVE);
        Matcher matcherHTML = patternHTML.matcher(postprocessDocument);
        postprocessDocument = matcherHTML.replaceAll("");
        return postprocessDocument.trim();
    }

    /**
     * The method getHtmlText() is to get the html string in the reader.
     *
     * @return The HTML string in the current reader.
     */
    public String getHtmlText() {
        return (String) webView.getEngine().executeScript("document.documentElement.outerHTML");
    }

    /**
     * The method setHtmlText() is to set the html string to the reader.
     *
     * @param htmlText The HTML string to set.
     */
    public void setHtmlText(String htmlText) {
        webView.getEngine().loadContent(htmlText);
    }

    /**
     * The method setReaderContent() is to set the content string to the reader.
     *
     * @param content The reader content to be set (perhaps including <span class="..."></span> labels).
     */
    public void setReaderContent(String content) {
        setHtmlText(preprocessHTMLDocument(content));
    }

    /**
     * The method getReaderContent() is to get the content string in the reader.
     *
     * @return content string without any html mark labels.
     */
    public String getReaderContent() {
        return postprocessHTMLDocument(getHtmlText());
    }

    /**
     * The method reload() is to reload the content in the reader.
     * It can clear the mark (highlight) in the reader.
     */
    public void reload() {
        setReaderContent(getReaderContent());
    }

    /**
     * The method initialize() is a default method called by Java FX Loader (after constructor method is called).
     * More details: https://newbedev.com/javafx-fxml-controller-constructor-vs-initialize-method
     */
    public void initialize() {
        fileListView.getSelectionModel().setSelectionMode(SINGLE);
        fileListView.setEditable(false);
        fileListView.setCellFactory(new Callback<ListView<FileCell>, ListCell<FileCell>>() {
            @Override
            public ListCell<FileCell> call(ListView<FileCell> fileCellListView) {
                return new ListCell<FileCell>() {
                    @Override
                    protected void updateItem(FileCell item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Label label = new Label(item.getFileName());
                            this.setOnMouseClicked(mouseEvent -> fileListItemOnSelected());
                            this.setGraphic(label);
                        }
                    }
                };
            }
        });

        webView.addEventFilter(KeyEvent.KEY_TYPED, Event::consume);
        webView.setOnInputMethodTextChanged(Event::consume);

        webView.getEngine().getLoadWorker().stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                updateWordsNumber();
            }
        });

        markStyle = ".mark{background-color:yellow;}";
        defaultStyle = ".default{color:black;}";

    }

    /**
     * Class FileCell is an abstract of a file item.
     */
    private static class FileCell {

        private File file;  // File object
        private String absoluteFilePath;    // The absolute path of the file
        private String fileName;    // the file name of the file
        private Integer wordsNumber;    // the words number in the file content
        private String fileContent;     // the file content in the file

        public FileCell(String filePath) {
            this.file = new File(filePath);
            this.initialize();
        }

        public FileCell(File file) {
            this.file = file;
            this.initialize();
        }

        /**
         * Initialize the attributes
         */
        private void initialize() {
            this.absoluteFilePath = this.file.getAbsolutePath();
            this.fileName = this.file.getName();

            if (!file.exists()) {
                this.wordsNumber = 0;
                this.fileContent = "";
            } else {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    this.fileContent = stringBuilder.toString();

                    //this.wordsNumber = this.fileContent.split("[?.!\\s]+").length;

                    Matcher m = Pattern.compile("\\b[\\w'-]+\\b").matcher(this.fileContent);
                    int num = 0;
                    while (m.find()) {
                        num++;
                    }
                    this.wordsNumber = num;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public File getFile() {
            return file;
        }

        public String getAbsoluteFilePath() {
            return absoluteFilePath;
        }

        public String getFileName() {
            return fileName;
        }

        public Integer getWordsNumber() {
            return wordsNumber;
        }

        public String getFileContent() {
            return fileContent;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public void setAbsoluteFilePath(String absoluteFilePath) {
            this.absoluteFilePath = absoluteFilePath;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setWordsNumber(Integer wordsNumber) {
            this.wordsNumber = wordsNumber;
        }

        public void setFileContent(String fileContent) {
            this.fileContent = fileContent;
        }
    }
}
