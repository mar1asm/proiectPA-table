package Controllers.GameComponents;

import Controllers.GameController;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static Controllers.GameController.PIP_HEIGHT;

public class PieseScoase extends Pane {

    public PieseScoase(int x, int y) {
        this.relocate(x, y);
        Rectangle rectangle = new Rectangle(0, 0, GameController.PIP_WIDTH + 1,
                PIP_HEIGHT +
                        1);

        rectangle.setFill(Color.valueOf("#522608"));
        this.getChildren().add(rectangle);
    }

    private List<Piece> pieces = new ArrayList<>();

    public void addPiece(Piece piece) {
        piece.setPosition(0, (int) (pieces.size() * (GameController.PIP_WIDTH * 0.7)));
        this.getChildren().add(piece);
        pieces.add(piece);
    }

    public Piece movePiece() {
        if (pieces.size() == 0)
            return null;
        return pieces.remove(pieces.size() - 1);
    }
}
