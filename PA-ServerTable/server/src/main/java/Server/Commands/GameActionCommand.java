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

public class GameActionCommand implements Command {
    @Override
    public Object execute(Object... params) throws IOException {
        Map<String, Object> response = new HashMap<>();
        if (params.length < 2) {
            response.put("code", 500);
            response.put("message", "Internal server error!");
            return response;
        }
        Map<String, Object> command = (HashMap<String, Object>) params[0];
        SelectionKey key = (SelectionKey) params[1];

        ClientInfo clientInfo = (ClientInfo) key.attachment();
        if (!clientInfo.isLoggedOn) {
            response.put("code", 400);
            response.put("message", "You are not logged in!");
            return response;
        }

        if (!clientInfo.inRoom) {
            response.put("code", 400);
            response.put("message", "You are not in a room!");
            return response;
        }

        if(!clientInfo.inGame) {
            response.put("code", 400);
            response.put("message", "The game hasn't started yet!");
            return response;
        }

        String roomKey = clientInfo.roomKey;

        try {
            Room room = RoomManager.getInstance().getRoom(roomKey);

            var other = room.otherPlayer(key);
            ObjectMapper objectMapper = new ObjectMapper();

            String notification = objectMapper.writeValueAsString(params);

            CommunicationMaster.sendToClient((SocketChannel) other.channel(), notification);

        } catch (RoomDoesntExistsException e) {
            e.printStackTrace();
        }

        response.put("code", 200);
        response.put("message", "Action successfully send o the other player!");
        return response;
    }

}
