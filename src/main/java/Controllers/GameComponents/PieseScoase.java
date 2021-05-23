package Controllers.GameComponents;

import Controllers.GameController;
import Controllers.GameState;
import Controllers.MoveController;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static javafx.application.Platform.runLater;

import static Controllers.GameController.PIP_HEIGHT;

public class PieseScoase extends Pane {

    private int index;

    public PieseScoase(int x, int y) {
        if(y == 0) index = -1;
        else index = 24;


        this.relocate(x, y);
        Rectangle rectangle = new Rectangle(0, 0, GameController.PIP_WIDTH + 1,
                PIP_HEIGHT +
                        1);

        rectangle.setFill(Color.valueOf("#522608"));
        this.getChildren().add(rectangle);
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleClick);
    }

    private List<Piece> pieces = new ArrayList<>();

    public void addPiece(Piece piece) {
        runLater(() -> {
            piece.setPosition(0, (int) (pieces.size() * (GameController.PIP_WIDTH * 0.7)));
            this.getChildren().add(piece);
        });

        pieces.add(piece);
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public Piece movePiece() {
        if (pieces.size() == 0)
            return null;
        return pieces.remove(pieces.size() - 1);
    }

    private void handleClick(MouseEvent handler) {
        if (!GameState.isMyTurn())
            return;
        try {
            MoveController.pipClicked(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
