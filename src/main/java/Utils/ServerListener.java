package Utils;

import Controllers.GameState;
import Utils.ServerConnection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Controllers.GameController.updateBoard;
import static Utils.ServerConnection.getFromServer;
import static Utils.ServerConnection.objectMapper;

public class ServerListener implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                String message = getFromServer();
                System.out.println(message);
                Map<String, Object> msg = objectMapper.readValue(message, HashMap.class);
                if (msg.get("action") == null)
                    continue;
                switch ((String) msg.get("action")) {
                    case "updateBoard":
                        updateBoard(message);
                        break;
                    case "turn":
                        String username = (String) msg.get("player");
                        int die1 = (Integer) msg.get("die1");
                        int die2 = (Integer) msg.get("die2");
                        GameState.setDice(die1, die2);
                        if (username.equals(GameState.getUsername())) {
                            GameState.setMyTurn(true);

                        } else {
                            GameState.setMyTurn(false);
                        }
                        // case "dice":
                        //     int die1 = Integer.parseInt((String) msg.get("die1"));
                        //     int die2 = Integer.parseInt((String) msg.get("die2"));
                        //     Game
                        //     // waitForMove();
                        //     break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
