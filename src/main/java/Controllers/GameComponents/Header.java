package Controllers.GameComponents;

import Controllers.GameController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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
    public Header() {

    }

    public Header(int heigth, int screenWidth) {
        this.setWidth(screenWidth);
        this.setHeight(heigth);
        this.relocate(0, 0);
        Label b = new Label("5 : 6");
        b.setFont(new Font("Arial", 30));
        this.relocate(screenWidth/2-30, 0);
        this.getChildren().add(b);


    }
}
