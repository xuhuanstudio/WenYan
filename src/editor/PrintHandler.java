package editor;

import compile.Handler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class PrintHandler implements Handler {

    protected final Label label;
    protected final ScrollPane scrollPane;

    public PrintHandler(Label label, ScrollPane scrollPane) {
        this.label = label;
        this.scrollPane = scrollPane;
    }

    @Override
    public void handle(Object param) {
        this.label.setText(this.label.getText() + param.toString());
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scrollPane.setVvalue(1);
        }).start();
    }
}
