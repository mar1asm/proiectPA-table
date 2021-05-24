package Controllers;

public class GameState {
    private static String gameID; //or room id
    private static String username;
    private static boolean isFinished = false;
    private static boolean hasStarted = false;

    private static boolean myTurn;

    private static int moveDirection;

    private static int die1;
    private static int die2;
    private static int die1Uses = 0;
    private static int die2Uses = 0;

    public static void setDice(int d1, int d2) {
        if(d1 != 0 && d2 != 0) {
            if(d1 == d2) {
                die1Uses = 2;
                die2Uses = 2;
            }
            else {
                die1Uses = 1;
                die2Uses = 1;
            }
        }
        if(d1 == 0) {
            die1Uses--;
            if(die1Uses == 0)
                die1 = 0;
        }
        else {
            die1 = d1;
        }
        if(d2 == 0) {
            die2Uses--;
            if(die2Uses == 0) {
                die2 = 0;
            }
        }
        else {
            die2 = d2;
        }
    }

    public static int getDie1() {
        return die1;
    }

    public static int getDie2() {
        return die2;
    }

    public static int getMoveDirection() {
        return moveDirection;
    }

    public static void setMoveDirection(int moveDirection) {
        GameState.moveDirection = moveDirection;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        GameState.username = username;
    }

    public static boolean isMyTurn() {
        return myTurn;
    }

    public static void setMyTurn(boolean myTurn) {
        GameState.myTurn = myTurn;
    }

    public static String getGameID() {
        return gameID;
    }

    public static void setGameID(String gameID) {
        GameState.gameID = gameID;
    }

    public static boolean isFinished() {
        return isFinished;
    }

    public static void setFinished(boolean finished) {
        isFinished = finished;
    }

    public static boolean isStarted() {
        return hasStarted;
    }

    public static void setHasStarted(boolean hasStarted) {
        GameState.hasStarted = hasStarted;
    }
}
