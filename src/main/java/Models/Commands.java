package Models;

import Controllers.GameState;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static Utils.ServerConnection.*;

public class Commands {

    public static void login(String username) throws IOException {
        Map<String, Object> loginRequest = new HashMap<>();
        Map<String, Object> loginParams = new HashMap<>();
        loginRequest.put("action", "Login");
        loginParams.put("name", username);
        loginRequest.put("params", loginParams);

        String loginRequestString = objectMapper.writeValueAsString(loginRequest);
        sendToServer(loginRequestString);


        String loginResponseString = getFromServer();

        System.out.println(loginResponseString);
    }

    public static void movePiece(int from, int to) throws IOException {
        Map<String, Object> moveRequest = new HashMap<>();
        Map<String, Object> moveParams = new HashMap<>();
        moveParams.put("from", from);
        moveParams.put("to", to);

        moveRequest.put("action", "MovePiece");
        moveRequest.put("params", moveParams);
        String createRoomString = objectMapper.writeValueAsString(moveRequest);


        sendToServer(createRoomString);

        //GameState.setMyTurn(false);

       //String createRoomResponseString = getFromServer();

       //System.out.println(createRoomResponseString);
    }
}
