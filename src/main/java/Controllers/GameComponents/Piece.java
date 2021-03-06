package Controllers.GameComponents;

import Controllers.GameController;
import Controllers.GameState;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends StackPane {
    private PieceType type;

    private Ellipse bg;

    public Piece(PieceType type) {
        this.type = type;
        bg = new Ellipse((double) (GameController.PIP_WIDTH * 0.35), (double) (GameController.PIP_WIDTH * 0.35));
        bg.setFill(type == PieceType.BLACK ? Color.valueOf("#2b2727") : Color.valueOf("#e8ca92"));
        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(1);
        this.getChildren().add(bg);
        this.setTranslateX((double) (GameController.PIP_WIDTH * 0.15));
        if(GameState.isMyTurn() && type.getMoveDirection() == GameState.getMoveDirection()) {
            highlight(true);
        }
    }

    public void dissapear() {
        bg.setFill(Color.rgb(0, 0, 0, 0));
        bg.setStrokeWidth(0);
    }

    public void highlight(boolean highlight) {
        if(highlight) {
            bg.setStrokeWidth(2);
            bg.setStroke(Color.YELLOW);
        }
        else {
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(1);
        }
    }

    public void setPosition( int x, int y) {
        relocate(x, y);
    }

    public int getMoveDirection(){
        return type.moveDirection;
    }

}