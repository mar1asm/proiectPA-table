package Controllers.GameComponents;

public enum PieceType {
    BLACK(1), WHITE(-1);
    final int moveDirection;

    PieceType (int moveDirection){
        this.moveDirection=moveDirection;
    }
    public int getMoveDirection(){
        return moveDirection;
    }
}
