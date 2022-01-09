package editor;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class PrintlnHandler extends PrintHandler {

    public PrintlnHandler(Label label, ScrollPane scrollPane) {
        super(label, scrollPane);
    }

    @Override
    public void handle(Object param) {
        String data = param.toString();
        String text = label.getText();
        if (!text.equals("") && !text.endsWith("\n")) {
            data = "\n" + data;
        }
        super.handle(data);
    }

}
