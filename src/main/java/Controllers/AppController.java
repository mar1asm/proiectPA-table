package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    private BorderPane mainFrame;

    private void loadPage(String page) {
        FxmlLoader loader = new FxmlLoader();
        Pane view = loader.getPage(page);
        mainFrame.setCenter(view);
    }

    public static void showGameWindow() throws IOException {
        Parent root = FXMLLoader.load(AppController.class.getResource("/fxml/GameView.fxml"));
        GameController gc = new GameController();

        Scene scene = new Scene(gc.createContent());
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();
        //gc.initGame();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPage("OnlineView");
    }

}
