package Server.Commands;

import Server.ClientInfo.ClientInfo;
import Server.RoomManager.Exceptions.RoomDoesntExistsException;
import Server.RoomManager.Room.Room;
import Server.RoomManager.RoomManager;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

public class CreateRoomCommand implements Command {
    @Override
    public Object execute(Object... params) throws IOException {
        Map<String, Object> response = new HashMap<>();
        if (params.length < 2) {
            response.put("code", 500);
            response.put("message", "Internal server error!");
            return response;
        }
        if(params[0] == null) {
            response.put("code", 400);
            response.put("message","Missing parameters for Create Room!");
            return response;
        }


        Map<String, Object> createRoomParams = (HashMap<String, Object>) params[0];
        SelectionKey key = (SelectionKey) params[1];

        ClientInfo clientInfo = (ClientInfo) key.attachment();
        if (!clientInfo.isLoggedOn) {
            response.put("code", 400);
            response.put("message", "You are not logged in!");
            return response;
        }
        if (clientInfo.inRoom) {
            response.put("code", 400);
            response.put("message", "You are already in a room!");
            return response;
        }

        if (!createRoomParams.containsKey("name")) {
            response.put("code", 400);
            response.put("message", "Needs <name> parameter!");
            return response;
        }

        RoomManager roomManager = RoomManager.getInstance();
        String name = (String) createRoomParams.get("name");
        int time = (int) createRoomParams.get("time");
        String roomKey = roomManager.createNewRoom(key, name, time);


        response.put("code", 200);
        response.put("message", "The room was created succesfully!");
        response.put("roomKey", roomKey);

        return response;
    }
}
