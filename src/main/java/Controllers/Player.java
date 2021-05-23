package Controllers;

public class Player {
    private String ID;
    private int playerType; //first or second 0-1

    public Player(String ID, int playerType) {
        this.ID = ID;
        this.playerType = playerType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }
}
