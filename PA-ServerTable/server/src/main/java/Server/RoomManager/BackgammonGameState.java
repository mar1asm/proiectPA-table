package Server.RoomManager;

import java.util.*;

public class BackgammonGameState {
    private class Pip {
        public int nbOfPieces;
        public PieceType pieceType;

        public void addPiece() {
            nbOfPieces++;
        }

        public void removePiece() {
            nbOfPieces--;
            if(nbOfPieces < 0) nbOfPieces = 0;
            if(nbOfPieces == 0) pieceType = null;
        }

        public void setPieceType(PieceType pieceType) {
            this.pieceType = pieceType;
        }

        public Map<String, Object> serializeToJson() {
            Map<String, Object> serializedPip = new HashMap<>();
            serializedPip.put("nb", nbOfPieces);
            serializedPip.put("type", pieceType);
            return serializedPip;
        }
    }



    private class RemovedPieces {
        int nbWhite = 0;
        int nbBlack = 0;

        public void addRemovedPiece(PieceType type) {
            if(type == PieceType.BLACK) {
                nbBlack++;
            }
            if(type == PieceType.WHITE) {
                nbWhite++;
            }
        }
        public void substractRemovedPiece(PieceType type) {
            if(type == PieceType.BLACK) {
                nbBlack--;
            }
            if(type == PieceType.WHITE) {
                nbWhite--;
            }
        }
    }



    private List<Pip> pips = new ArrayList<>();

    private RemovedPieces removedPieces = new RemovedPieces();
    private RemovedPieces finishedPieces = new RemovedPieces();

    public int dice1 = 0, dice2 = 0;
    public int dice1Uses = 0, dice2Uses = 0;
    private int turn;



    private boolean isHome() {
        if(turn == 1) {
            if(removedPieces.nbBlack > 0) return false;

            for(int i = 6 ; i < 24; ++i) {
                if(pips.get(i).pieceType == PieceType.BLACK) return false;
            }

            return true;
        }

        if(turn == 0) {
            if(removedPieces.nbWhite > 0) return false;

            for(int i = 0 ; i < 18; ++i) {
                if(pips.get(i).pieceType == PieceType.WHITE) return false;
            }
            return true;
        }
        return false;
    }

    public void ThrowDice() {

        Random r = new Random();
        dice1 = r.nextInt(6) + 1;
        dice2 = r.nextInt(6) + 1;
//        dice1 = 1;
//        dice2 = 1;
        if(dice1 == dice2) {
            dice1Uses = 2;
            dice2Uses = 2;
        }
        else {
            dice1Uses = 1;
            dice2Uses = 1;
        }


    }
    public void nextTurn() {
        turn = (turn + 1) % 2;
        ThrowDice();
    }

    private boolean checkPossibleMove(int from, int die) {
        if (die == 0)
            return false;
        int moveDirection = (turn == 0) ? 1 : -1;
        int to = from + die * moveDirection;

        if(isHome()) {
            if(to < 0 && turn == 1) {
                return true;
            }
            if(to > 23 && turn == 0) {
                return true;
            }
        }
        int pieceTypeFrom;
        if (from == -1)
            pieceTypeFrom = 1;
        else if (from == 24)
            pieceTypeFrom = -1;
        else
            pieceTypeFrom = pips.get(from).pieceType.getMoveDirection();
        return to <= 23 && to >= 0 && (pips.get(to).nbOfPieces == 0 || pips.get(to).pieceType.getMoveDirection() == pieceTypeFrom || pips.get(to).nbOfPieces == 1);
    }

    public boolean checkPossibilityToMove() {
        if(turn == 0) {
            if(removedPieces.nbWhite >0) {
                if(checkPossibleMove(-1, dice1)) {
                    return true;
                }
                if(checkPossibleMove(-1, dice2)) {
                    return true;
                }

                return  false;
            }
        }
        if(turn == 1) {
            if(removedPieces.nbBlack > 0) {
                if(checkPossibleMove(24, dice1)) {
                    return true;
                }
                if(checkPossibleMove(24, dice2)) {
                    return true;
                }
            }
        }

        PieceType pieceType = (turn == 0) ? PieceType.WHITE : PieceType.BLACK;
        for(int i = 0 ; i < 24; ++i) {
            var pip = pips.get(i);
            if(pip.pieceType == pieceType) {
                if(checkPossibleMove(i, dice1)) {
                    return true;
                }
                if(checkPossibleMove(i, dice2)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void removeDice(int value) {
        if(dice1 == value) {
            dice1Uses--;
            if(dice1Uses == 0) {
                dice1 = 0;
            }
            return;
        }
        if(dice2 == value) {
            dice2Uses--;
            if(dice2Uses == 0) {
                dice2 = 0;
            }
            return;
        }
    }

    public int getTurn() {
        return turn;
    }

    public void MovePiece(int pip1index, int pip2index) {

        if(pip2index == -2 || pip2index == 25) {
            Pip pip1 = pips.get(pip1index);
            pip1.removePiece();
            if(pip2index == 25) {
                finishedPieces.addRemovedPiece(PieceType.WHITE);
            }
            if(pip2index == -2) {
                finishedPieces.addRemovedPiece(PieceType.BLACK);
            }
            return;
        }


        Pip pip2 = pips.get(pip2index);



        if(pip1index == -1) {
            removedPieces.substractRemovedPiece(PieceType.WHITE);
            if(pip2.pieceType != null && pip2.pieceType != PieceType.WHITE) {
                removedPieces.addRemovedPiece(pip2.pieceType);
                pip2.removePiece();
            }
            pip2.pieceType = PieceType.WHITE;
            pip2.addPiece();
            return;
        }


        if(pip1index == 24) {
            removedPieces.substractRemovedPiece(PieceType.BLACK);
            if(pip2.pieceType != null && pip2.pieceType != PieceType.BLACK) {
                removedPieces.addRemovedPiece(pip2.pieceType);
                pip2.removePiece();
            }
            pip2.pieceType = PieceType.BLACK;
            pip2.addPiece();
            return;
        }




        Pip pip1 = pips.get(pip1index);



        if(pip2.pieceType != null && pip2.pieceType != pip1.pieceType) {
            removedPieces.addRemovedPiece(pip2.pieceType);
            pip2.removePiece();
        }


        pip2.pieceType = pip1.pieceType;
        pip1.removePiece();
        pip2.addPiece();
    }

    public void initGame() {

        for (int i = 0; i < 24; ++i) {
            pips.add(new Pip());
        }

        pips.get(5).setPieceType(PieceType.BLACK);
        for (int i = 0; i < 5; i++) {

            pips.get(5).addPiece();
        }


        pips.get(7).setPieceType(PieceType.BLACK);

        for (int i = 0; i < 3; i++) {
            pips.get(7).addPiece();
        }

        pips.get(12).setPieceType(PieceType.BLACK);
        for (int i = 0; i < 5; i++) {

            pips.get(12).addPiece();
        }

        pips.get(11).setPieceType(PieceType.WHITE);
        for (int i = 0; i < 5; i++) {
            pips.get(11).addPiece();
        }


        pips.get(23).setPieceType(PieceType.BLACK);
        for (int i = 0; i < 2; i++) {
            pips.get(23).addPiece();
        }


        pips.get(0).setPieceType(PieceType.WHITE);
        for (int i = 0; i < 2; ++i) {
            pips.get(0).addPiece();
        }


        pips.get(16).setPieceType(PieceType.WHITE);
        for (int i = 0; i < 3; i++) {
            pips.get(16).addPiece();
        }

        pips.get(18).setPieceType(PieceType.WHITE);
        for (int i = 0; i < 5; i++) {

            pips.get(18).addPiece();
        }

//        pips.get(1).setPieceType(PieceType.BLACK);
//        pips.get(1).addPiece();
//        pips.get(1).addPiece();
//        pips.get(17).setPieceType(PieceType.BLACK);
//        pips.get(17).addPiece();
//        pips.get(17).addPiece();
//        pips.get(19).setPieceType(PieceType.BLACK);
//        pips.get(19).addPiece();
//        pips.get(19).addPiece();

//        pips.get(1).setPieceType(PieceType.BLACK);
//        pips.get(1).addPiece();
//        pips.get(2).setPieceType(PieceType.BLACK);
//        pips.get(2).addPiece();
//        pips.get(2).setPieceType(PieceType.BLACK);
//        pips.get(2).addPiece();
//        pips.get(3).setPieceType(PieceType.BLACK);
//        pips.get(3).addPiece();
//        pips.get(4).setPieceType(PieceType.BLACK);
//        pips.get(4).addPiece();
//        pips.get(5).setPieceType(PieceType.BLACK);
//        pips.get(5).addPiece();
//
//        pips.get(23).setPieceType(PieceType.WHITE);
//        pips.get(23).addPiece();
//        pips.get(22).setPieceType(PieceType.WHITE);
//        pips.get(22).addPiece();
//        pips.get(21).setPieceType(PieceType.WHITE);
//        pips.get(21).addPiece();
//        pips.get(20).setPieceType(PieceType.WHITE);
//        pips.get(20).addPiece();
//        pips.get(19).setPieceType(PieceType.WHITE);
//        pips.get(19).addPiece();
//        pips.get(18).setPieceType(PieceType.WHITE);
//        pips.get(18).addPiece();


        turn = 0;
        ThrowDice();
        //pt testarea pieselor mancate
        //removedPieces.addRemovedPiece(PieceType.WHITE);
    }


    public Map<String, Object> serializeToJson() {
        Map<String, Object> serializedGameState = new HashMap<>();

        for(int i = 0 ; i < 24; ++i) {
            Integer integer = i;
            serializedGameState.put(integer.toString(), pips.get(i).serializeToJson());
        }

        serializedGameState.put("removedWhite", removedPieces.nbWhite);
        serializedGameState.put("removedBlack", removedPieces.nbBlack);

        serializedGameState.put("finishedBlack", finishedPieces.nbBlack);
        serializedGameState.put("finishedWhite", finishedPieces.nbWhite);

        return serializedGameState;
    }


}


