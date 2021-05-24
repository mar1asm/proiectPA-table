package Server.Commands;

import Server.ClientInfo.ClientInfo;
import Server.CommunicationMaster.CommunicationMaster;
import Server.RoomManager.Exceptions.RoomDoesntExistsException;
import Server.RoomManager.Room.Room;
import Server.RoomManager.RoomManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class StartGameCommand implements Command{
    @Override
    public Object execute(Object... params) throws IOException {
        Map<String, Object> response = new HashMap<>();
        if (params.length < 2) {
            response.put("code", 500);
            response.put("message", "Internal server error!");
            return response;
        }

        SelectionKey key = (SelectionKey) params[1];

        ClientInfo clientInfo = (ClientInfo) key.attachment();
        if (!clientInfo.isLoggedOn) {
            response.put("code", 400);
            response.put("message", "You are not logged in!");
            return response;
        }

        if(!clientInfo.inRoom) {
            response.put("code", 400);
            response.put("message", "You are not in a room!");
            return response;
        }

        if(clientInfo.inGame) {
            response.put("code", 400);
            response.put("message", "You are already in a game!");
            return response;
        }
        String roomKey = clientInfo.roomKey;
        RoomManager roomManager = RoomManager.getInstance();

        Room room = null;
        try {
            room = roomManager.getRoom(roomKey);
        } catch (RoomDoesntExistsException e) {
            e.printStackTrace();
        }

        if(!room.full()) {
            response.put("code", 400);
            response.put("message", "The room doesn't have enough players!");
            return response;
        }

        if(!room.isHost(key)) {
            response.put("code", 400);
            response.put("message", "You are not the host, you can't start the game!");
        }

        room.startGame();


        response.put("code", 200);
        response.put("message","Successfully started the game!");



        var other = room.otherPlayer(key);

        Map<String, Object> notification = new HashMap<>();

        notification.put("event", "gameStarted");
        ObjectMapper objectMapper = new ObjectMapper();

        CommunicationMaster.sendToClient((SocketChannel) other.channel(), objectMapper.writeValueAsString(notification));

        return response;
    }
}
