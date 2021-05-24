package Controllers.GameComponents;

import Controllers.GameController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import static Controllers.GameController.PIP_HEIGHT;

public class Header extends Pane {
    private static TextArea log;
    public Header() {

    }

    public Header(int screenHeight, int screenWidth) {
        this.setWidth(200);
        this.setHeight(screenHeight);
        this.relocate(screenWidth-10, 0);
       // Label b = new Label("5 : 6");
       // b.setFont(new Font("Arial", 30));
       // this.relocate(screenWidth/2-30, 0);

        log = new TextArea();
        log.setEditable(false);
        log.setFont(new Font("Arial", 20));
        log.setPrefSize(200, screenWidth);


        this.getChildren().add(log);
        //this.getChildren().add(b);
    }

    public static void clearText(){
        log.clear();
    }

    public static void appendText(String text){
        log.appendText(text+"\n");
    }
}
