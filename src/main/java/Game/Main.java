package Game;

import Controllers.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/App.fxml"));
        primaryStage.setTitle("Backgammon");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();

        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/GameView.fxml"));
        //GameController gc = new GameController();
//
        //Scene scene = new Scene(gc.createContent());
        //primaryStage.setScene(scene);
        //primaryStage.show();
        //gc.initGame();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
