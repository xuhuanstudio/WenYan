package editor;

import ast.ProgarmNode;
import com.jfoenix.controls.JFXToggleButton;
import common.FileUtil;
import compile.*;
import compile.Compiler;
import compile.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.NavigationActions;
import org.fxmisc.richtext.StyleClassedTextArea;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor implements Initializable {

    public Compiler compiler = new Compiler();
    public CodeLoader codeLoader = new CodeLoader();

    public String code = "";
    public ArrayList<Token> tokens = new ArrayList<>();
    public ProgarmNode ast = new ProgarmNode();
    public String newCode = "";
    public String projectDirectories = "";
    public String currentFilePath = "";

    public StyleClassedTextArea styleClassedTextArea;

    public VBox codeAreaParent;
    public VBox mainNode;
    public HBox topBar;
    public MenuBar topMenuBar;
    public Label consoleArea;
    public Button windowFormsBtn;
    public Button maximizeFormsBtn;
    public ListView fileListView;
    public Label pathLabel;
    public Label projectNameLabel;
    public JFXToggleButton debugToggleButton;
    public ScrollPane consoleScrollPlane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DragListener dragListener = new DragListener(Main.primaryStage);
        dragListener.enableDrag(topBar);

        windowFormsBtn.setVisible(false);
        maximizeFormsBtn.setVisible(true);

        // 初始化代码编辑面板
        styleClassedTextArea = new StyleClassedTextArea();
        styleClassedTextArea.setLineHighlighterOn(true);
        styleClassedTextArea.getStyleClass().add("code-area");
        // 侦听内容变化，以实时高亮标注词法
        styleClassedTextArea.caretPositionProperty().addListener((observable, oldValue, newValue) -> {
            this.code = styleClassedTextArea.getText();
            Tokenizer tokenizer = new Tokenizer();

            try {
                tokenizer.parse(this.code);
            } catch (Exception exception) {
//                exception.printStackTrace();
            }
            styleClassedTextArea.setStyleClass(0, this.code.length(), "white");

            if (tokenizer.getTokens() == null) {
                styleClassedTextArea.setStyleClass(0, this.code.length(), "error");
            } else {
                // 高亮词义
                for (Token token : tokenizer.getTokens()) {
                    styleClassedTextArea.setStyleClass(
                            token.getStart(), token.getEnd(),
                            token.getType().toString());
                }


                // 标红错误
                if (tokenizer.getTokens().size() > 0) {
                    Token token = tokenizer.getTokens().get(tokenizer.getTokens().size() - 1);
                    int start = token.getEnd();
                    Matcher matcher = Pattern.compile("^((\\s|\\n)*)((，|。)?)((\\s|\\n)*)").matcher(this.code.substring(start));
                    if (matcher.find()) {
                        start += matcher.group(0).length();
                    }

                    styleClassedTextArea.setStyleClass(start, this.code.length(), "error");
                }
            }
        });
        codeAreaParent.getChildren().add(styleClassedTextArea);

        // 初始化控制台
        initConsolePrint();
        // 初始化文件列表
        initFileListView();
    }

    public void onCompile(ActionEvent actionEvent) {
        this.clearConsole();

        try {
            this.newCode = compiler.compile(styleClassedTextArea.getText());
            Output.debugPrintln("【编译完成】");
        } catch (Exception exception) {
            Output.debugPrintln("【编译失败】");
            Output.debugPrintln(exception);
        }
    }

    public void onRun(ActionEvent actionEvent) {
        this.clearConsole();

        try {
            codeLoader.invoke(this.newCode);
//            Output.debugPrintln("运行结束");
        } catch (Exception exception) {
            exception.printStackTrace();
            Output.debugPrintln("【运行失败】");
        }
    }

    public void initConsolePrint() {
        EventManager.addListener(Event.Print, new PrintHandler(this.consoleArea, this.consoleScrollPlane));
        EventManager.addListener(Event.Println, new PrintlnHandler(this.consoleArea, this.consoleScrollPlane));
    }

    public void initFileListView() {
        fileListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue == null) return;
                currentFilePath = projectDirectories + "\\" + ((Label)newValue).getText();
                String data = FileUtil.read(currentFilePath);

                if (data == null) {
                    Output.debugPrintln("文件打开失败");
                } else {
                    pathLabel.setText(currentFilePath);
                    styleClassedTextArea.deleteText(0, styleClassedTextArea.getText().length());
                    styleClassedTextArea.insertText(0, data);
                }
            }
        });
    }

    public void clearConsole() {
        this.consoleArea.setText("");
    }

    public void onMinimizeForms(ActionEvent actionEvent) {
        Main.primaryStage.setIconified(true);
    }

    public void onMaximizeForms(ActionEvent actionEvent) {
        Main.primaryStage.setMaximized(true);
        windowFormsBtn.setVisible(true);
        maximizeFormsBtn.setVisible(false);
    }

    public void onWindowForms(ActionEvent actionEvent) {
        Main.primaryStage.setMaximized(false);
        windowFormsBtn.setVisible(false);
        maximizeFormsBtn.setVisible(true);
    }

    public void onCloseForms(ActionEvent actionEvent) {
        System.exit(0);
    }

    private void loadFileList() {
        // 清空列表
        fileListView.getItems().removeAll(fileListView.getItems());

        File file = new File(projectDirectories);
        projectNameLabel.setText(file.getName());

        File[] files = file.listFiles();

        for (File _file : files) {
            if (_file.isFile() && _file.getName().endsWith(".wy")) {
                fileListView.getItems().add(new Label(_file.getName()));
            }
        }
    }

    public void onOpenFolder(ActionEvent actionEvent) {
        JFileChooser jFileChooser = new JFileChooser();
        FileSystemView fileSystemView = jFileChooser.getFileSystemView();
        jFileChooser.setCurrentDirectory(fileSystemView.getHomeDirectory());
        jFileChooser.setDialogTitle("请选择项目文件夹");
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
            projectDirectories = jFileChooser.getSelectedFile().getPath();
            loadFileList();
        } else {
            Output.debugPrintln("文件夹打开失败");
        }
    }

    public void onOpenFile(ActionEvent actionEvent) {
        // 清空列表
        fileListView.getItems().removeAll(fileListView.getItems());

        JFileChooser jFileChooser = new JFileChooser();
        FileSystemView fileSystemView = jFileChooser.getFileSystemView();
        jFileChooser.setCurrentDirectory(fileSystemView.getHomeDirectory());
        jFileChooser.setDialogTitle("请选择文件");
        jFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return (f.isDirectory() || f.getName().endsWith(".wy"));
            }

            @Override
            public String getDescription() {
                return ".wy";
            }
        });
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (JFileChooser.APPROVE_OPTION == jFileChooser.showOpenDialog(null)) {
            projectDirectories = "";
            projectNameLabel.setText("");
            currentFilePath = jFileChooser.getSelectedFile().getPath();
            pathLabel.setText(currentFilePath);
        }

        String data = FileUtil.read(currentFilePath);
        if (data == null) {
            Output.debugPrintln("文件打开失败");
        } else {
            styleClassedTextArea.deleteText(0, styleClassedTextArea.getText().length());
            styleClassedTextArea.insertText(0, data);
        }

    }

    public void onSaveFile(ActionEvent actionEvent) throws IOException {
        boolean isNewFile = false;
        if (currentFilePath.equals("")) {
            // 新建文件
            isNewFile = true;

            JFileChooser jFileChooser = new JFileChooser();
            FileSystemView fileSystemView = jFileChooser.getFileSystemView();
            jFileChooser.setCurrentDirectory(fileSystemView.getHomeDirectory());
            jFileChooser.setDialogTitle("请选择项目文件夹");
            jFileChooser.setFileFilter(new FileNameExtensionFilter("*.wy", "wy"));
            jFileChooser.setMultiSelectionEnabled(false);

            if (JFileChooser.APPROVE_OPTION == jFileChooser.showSaveDialog(null)) {
                File file = jFileChooser.getSelectedFile();

                if (!file.getPath().endsWith(".wy")) {
                    file = new File(file.getPath()+".wy");
                }

                if (!file.exists()) {
                    file.createNewFile();
                }

                currentFilePath = file.getCanonicalPath();
                pathLabel.setText(currentFilePath);
            }

        }

        boolean isSave = FileUtil.save(currentFilePath, styleClassedTextArea.getText());
        Output.debugPrintln(String.format("文件保存%s", isSave ? "成功" : "失败"));

        if (isNewFile && isSave && new File(currentFilePath).getParent().equals(projectDirectories)) {
            System.out.println("刷新文件列表");
            loadFileList();
        }
    }

    public void onSwitchDebug(ActionEvent actionEvent) {
        boolean isOn = debugToggleButton.isSelected();
        compiler.setDebug(isOn);
        codeLoader.setIsDebug(isOn);
    }

    public void onNewFile(ActionEvent actionEvent) {
        styleClassedTextArea.deleteText(0, styleClassedTextArea.getText().length());
//        projectDirectories = "";
//        projectNameLabel.setText("");
//        fileListView.getItems().removeAll(fileListView.getItems());
        pathLabel.setText("");
        currentFilePath = "";
    }

    public void onOpenDocument(ActionEvent actionEvent) throws IOException, URISyntaxException {
        File file = new File("./document/document.pdf");
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /c start " + file.getCanonicalPath());
    }

    public void onAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("关于");
        alert.setHeaderText("WenYan V1.0");
        alert.setContentText("作者: 虚幻\nQQ: 2595666958");
        alert.showAndWait();
    }
}
