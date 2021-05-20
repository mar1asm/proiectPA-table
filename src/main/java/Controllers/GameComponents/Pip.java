package Controllers.GameComponents;

import Controllers.GameController;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Pip extends Pane {

    private int index;

    private static class Triangle extends Polygon {

        Triangle(Point2D v1, Point2D v2, Point2D v3, Color color) {
            this.getPoints().addAll(v1.getX(), v1.getY(), v2.getX(), v2.getY(), v3.getX(), v3.getY());
            setFill(color);
        }
    }

    private List<Piece> pieces = new ArrayList<>();

    public Pip(int index, Color color, int x, int y, boolean isReversed) {
        this.index = index;
        this.setWidth(GameController.PIP_WIDTH);
        this.setHeight(GameController.PIP_HEIGHT);
        this.relocate(x, y);
        if (isReversed)
            this.setRotate(180);
        Rectangle rectangle = new Rectangle(0, 0, GameController.PIP_WIDTH+2, GameController.PIP_HEIGHT+2); //marele workaround
        rectangle.setFill(Color.valueOf("#996c54"));
        this.getChildren().add(rectangle);
        Triangle triangle = new Triangle(new Point2D(0, 0), new Point2D(GameController.PIP_WIDTH, 0), new Point2D((int) ((GameController.PIP_WIDTH) /
                2), (int) (GameController.PIP_HEIGHT / 20 * 19)), color);
        this.getChildren().add(triangle);
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleClick);
    }

    public boolean isEmpty() {
        return pieces.size() == 0;
    }

    public int getPiecesType() {
        if (isEmpty())
            return 0;
        return pieces.get(0).getMoveDirection();
    }

    public void addPiece(Piece piece) {
        piece.setPosition(0, (int) (pieces.size() * (GameController.PIP_WIDTH * 0.7)));
        this.getChildren().add(piece);
        pieces.add(piece);
    }

    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
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
        System.out.println("Clicked");
       // MoveController.pipClicked(index);
    }


}
