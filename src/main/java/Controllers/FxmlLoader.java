package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class FxmlLoader {
    private Pane view;

    public Pane getPage(String fileName) {
        try {
            URL fileUrl = getClass().getResource("/fxml/" + fileName + ".fxml");
            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML can't be loaded");
            }
            view = FXMLLoader.load(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }
}