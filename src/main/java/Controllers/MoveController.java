package Controllers;

import Controllers.GameComponents.Piece;
import Controllers.GameComponents.PieceType;
import Controllers.GameComponents.PieseScoase;
import Controllers.GameComponents.Pip;
import Utils.ServerConnection;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Controllers.GameController.*;
import static Models.Commands.movePiece;

public class MoveController {
    private static List<Pip> pips;
    private static int from = -3;
    private static int to = -1;

    MoveController(List<Pip> pips) {
        MoveController.pips = pips;
    }


    public static void highlightPieces(boolean highlight) {
        for (var pip : pips) {
            if (pip.getPiecesType() == GameState.getMoveDirection()) {
                for (var piece : pip.getPieces()) {
                    piece.highlight(highlight);
                }
            }
        }
    }

    private static boolean checkPossibleMove(int from, int die) {
        if (die == 0)
            return false;
        int to = from + die * GameState.getMoveDirection();
        int pieceTypeFrom;

        if (isHome()) {
            if (to < 0 && GameState.getMoveDirection() == -1)
                return true;
            if (to > 23 && GameState.getMoveDirection() == 1)
                return true;
        }
        if (to < 0 && GameState.getMoveDirection() == -1 && isHome())
            return true;
        if (to > 23 && GameState.getMoveDirection() == -1 && isHome())
            return true;

        if (from == -1)
            pieceTypeFrom = 1;
        else if (from == 24)
            pieceTypeFrom = -1;
        else
            pieceTypeFrom = pips.get(from).getPiecesType();
        return to <= 23 && to >= 0 && (pips.get(to).isEmpty() || pips.get(to).getPiecesType() == pieceTypeFrom || pips.get(to).getPieces().size() == 1);

    }

    public static boolean checkPossibilityToMove() {
        for (int i = 0; i < 24; ++i) {
            var pip = pips.get(i);
            if (pip.getPiecesType() == GameState.getMoveDirection()) {
                if (checkPossibleMove(i, GameState.getDie1())) {
                    return true;
                }
                if (checkPossibleMove(i, GameState.getDie2())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isHome() {
        if (
                GameState.getMoveDirection() == 1) {
            for (int i = 6; i < 24; i++)
                if (pips.get(i).getPiecesType() == 1)
                    return false;
            return true;
        }
        if (GameState.getMoveDirection() == -1) {
            for (int i = 0; i < 18; i++)
                if (pips.get(i).getPiecesType() == -1)
                    return false;
            return true;
        }
        return true;
    }

    public static void pipClicked(int index) throws IOException {
        System.out.println("AUUUUU");
        if (index == -1 || index == 24) {
            if (index == -1 && GameState.getMoveDirection() != 1) return;
            if (index == 24 && GameState.getMoveDirection() != -1) return;


        }

        if (GameState.getMoveDirection() == 1 && pieseScoaseAlb.getPieces().size() != 0 && index != -1 && from == -3)
            return;
        if (GameState.getMoveDirection() == -1 && pieseScoaseNegru.getPieces().size() != 0 && index != 24 && from == -3)
            return;

        int toDie1 = index + GameState.getDie1() * GameState.getMoveDirection();
        int toDie2 = index + GameState.getDie2() * GameState.getMoveDirection();

        if (from == index) {
            pips.get(toDie1).setHighlighted(false);
            pips.get(toDie2).setHighlighted(false);
            from = -3;
            return;
        }
        if (from == -3) {
            if (index != -1 && index != 24)
                if (pips.get(index).getPiecesType() != GameState.getMoveDirection())
                    return;
            from = index;
            boolean movesPossible = false;
            if (checkPossibleMove(from, GameState.getDie1())) {
                pips.get(toDie1).setHighlighted(true);
                movesPossible = true;
            }
            if (checkPossibleMove(from, GameState.getDie2())) {
                pips.get(toDie2).setHighlighted(true);
                movesPossible = true;
            }
            if (!movesPossible)
                from = -3;
            return;
        }
        if (!pips.get(index).isHighlighted())
            return;
        pips.get(from+ GameState.getDie1() * GameState.getMoveDirection()).setHighlighted(false);
        pips.get(from+ GameState.getDie2() * GameState.getMoveDirection()).setHighlighted(false);
        to = index;
        move(from, to);
        from = -3;
        to = -1;
    }


    private static void move(int from, int to) throws IOException {
        if (Math.abs(to - from) == GameState.getDie1())
            GameState.setDice(0, GameState.getDie2());
        else if (Math.abs(to - from) == GameState.getDie2())
            GameState.setDice(GameState.getDie1(), 0);
        movePiece(from, to);

        if (GameState.getDie1() == 0 && GameState.getDie2() == 0) {
            GameState.setMyTurn(false);
            Map<String, Object> nextTurnRequest = new HashMap<>();
            nextTurnRequest.put("action", "NextTurn");
            String message = ServerConnection.objectMapper.writeValueAsString(nextTurnRequest);
            //ServerConnection.sendToServer(message);
        }

//       Piece piece = pips.get(from).movePiece();
//        pips.get(to).addPiece(piece);
    }

}
