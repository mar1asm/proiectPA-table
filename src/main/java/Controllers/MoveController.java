package Controllers;
import Controllers.GameComponents.Piece;
import Controllers.GameComponents.PieseScoase;
import Controllers.GameComponents.Pip;

import java.io.IOException;
import java.util.List;

import static Models.Commands.movePiece;

public class MoveController {
    private static List<Pip> pips;
    private static int from = -1;
    private static int to = -1;

    MoveController(List<Pip> pips) {
        MoveController.pips = pips;
    }

    public static void pipClicked(int index) throws IOException {
        if (from == index) {
            from = -1;
            return;
        }
        if (from == -1) {
            from = index;
            return;
        }
        to = index;
        move(from, to);
        from = -1;
        to = -1;
    }


    private static void move(int from, int to) throws IOException {
        if (validateMove(from, to)) {
            movePiece(from, to);

            Piece piece = pips.get(from).movePiece();
            pips.get(to).addPiece(piece);
        }
    }

    public static boolean validateMove(int from, int to) {  /////dice validation
        if (to < 0 || to > 23)
            return false;
        if (pips.get(from).getPiecesType() == -1 && to < from)
            return false;
        if (pips.get(from).getPiecesType() == 1 && to > from)
            return false;

        if (pips.get(from).isEmpty())
            return false;
        if (pips.get(to).isEmpty())
            return true;
        if (pips.get(to).getPieces().size() == 1 && pips.get(to).getPiecesType() != pips.get(from).getPiecesType()) {
            PieseScoase pieseScoase = GameController.getPieseScoase(pips.get(to).getPiecesType());
            Piece piece = pips.get(to).movePiece();
            pieseScoase.addPiece(piece);
            return true;
        }
        return pips.get(from).getPiecesType() == pips.get(to).getPiecesType();
    }

}
