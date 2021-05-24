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
    private static int from = -2;
    private static int to = -1;

    MoveController(List<Pip> pips) {
        MoveController.pips = pips;
    }


    public static void highlightPieces(boolean highlight) {
        for(var pip : pips) {
            if(pip.getPiecesType() == GameState.getMoveDirection()) {
                for(var piece : pip.getPieces()) {
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
        if (from == -1)
            pieceTypeFrom = 1;
        else if (from == 24)
            pieceTypeFrom = -1;
        else
            pieceTypeFrom = pips.get(from).getPiecesType();
        return to <= 23 && to >= 0 && (pips.get(to).isEmpty() || pips.get(to).getPiecesType() == pieceTypeFrom || pips.get(to).getPieces().size() == 1);

    }

    public static boolean checkPossibilityToMove() {

        if(GameState.getMoveDirection() == PieceType.BLACK.getMoveDirection()) {
            if(pieseScoaseNegru.getPieces().size() > 0) {
                if(checkPossibleMove(24, GameState.getDie1())) {
                    return  true;
                }
                if(checkPossibleMove(24, GameState.getDie2())) {
                    return  true;
                }
                return false;
            }
        }
        if(GameState.getMoveDirection() == PieceType.WHITE.getMoveDirection()) {
            if(pieseScoaseAlb.getPieces().size() > 0) {
                if(checkPossibleMove(-1, GameState.getDie1())) {
                    return  true;
                }
                if(checkPossibleMove(-1, GameState.getDie2())) {
                    return  true;
                }
                return false;
            }
        }


        for(int i = 0 ; i < 24; ++i) {
            var pip = pips.get(i);
            if(pip.getPiecesType() == GameState.getMoveDirection()) {
                if(checkPossibleMove(i, GameState.getDie1())) {
                    return true;
                }
                if(checkPossibleMove(i, GameState.getDie2())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void pipClicked(int index) throws IOException {
        if (index == -1 || index == 24) {
            if (index == -1 && GameState.getMoveDirection() != 1) return;
            if (index == 24 && GameState.getMoveDirection() != -1) return;


        }

        if (GameState.getMoveDirection() == 1 && pieseScoaseAlb.getPieces().size() != 0 && index != -1 && from == -2) return;
        if (GameState.getMoveDirection() == -1 && pieseScoaseNegru.getPieces().size() != 0 && index != 24 && from == -2) return;


        if (from == index) {
            pips.get(from + GameState.getDie1()).setHighlighted(false);
            pips.get(from + GameState.getDie2()).setHighlighted(false);
            from = -2;
            return;
        }
        if (from == -2) {
            if (index != -1 && index != 24)
                if (pips.get(index).getPiecesType() != GameState.getMoveDirection())
                    return;
            from = index;
            if (checkPossibleMove(from, GameState.getDie1()))
                pips.get(from + GameState.getDie1() * GameState.getMoveDirection()).setHighlighted(true);
            if (checkPossibleMove(from, GameState.getDie2()))
                pips.get(from + GameState.getDie2() * GameState.getMoveDirection()).setHighlighted(true);
            return;
        }
        if (!pips.get(index).isHighlighted())
            return;
        int indexPip1 = from + GameState.getDie1() * GameState.getMoveDirection();
        if(indexPip1 >= 0 && indexPip1 < 24)
            pips.get(indexPip1).setHighlighted(false);

        int indexPip2 = from + GameState.getDie2() * GameState.getMoveDirection();
        if(indexPip2 >= 0 && indexPip2 < 24)
            pips.get(indexPip2).setHighlighted(false);
        to = index;
        move(from, to);

        from = -2;
        to = -1;
    }


    private static void move(int from, int to) throws IOException {
        if (Math.abs(to - from) == GameState.getDie1())
            GameState.setDice(0, GameState.getDie2()); else
        if (Math.abs(to - from) == GameState.getDie2())
            GameState.setDice(GameState.getDie1(), 0);
        movePiece(from, to);

        if (GameState.getDie1() == 0 && GameState.getDie2() == 0) {
            GameState.setMyTurn(false);
            Map<String, Object> nextTurnRequest = new HashMap<>();
            nextTurnRequest.put("action", "NextTurn");
            String message = ServerConnection.objectMapper.writeValueAsString(nextTurnRequest);
            ServerConnection.sendToServer(message);
        }

//       Piece piece = pips.get(from).movePiece();
//        pips.get(to).addPiece(piece);
    }

}
