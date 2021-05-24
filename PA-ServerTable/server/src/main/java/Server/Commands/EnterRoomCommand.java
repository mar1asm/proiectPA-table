package Server.Commands;

import Server.ClientInfo.ClientInfo;
import Server.CommunicationMaster.CommunicationMaster;
import Server.RoomManager.Exceptions.RoomDoesntExistsException;
import Server.RoomManager.Room.Exceptions.FullRoomException;
import Server.RoomManager.Room.Exceptions.PlayerAlreadyInRoomException;
import Server.RoomManager.Room.Room;
import Server.RoomManager.RoomManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class EnterRoomCommand implements Command{

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
            response.put("message","Missing parameters for Enter room!");
            return response;
        }


        Map<String, Object> enterRoomParams = (HashMap<String, Object>) params[0];
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

        if(!enterRoomParams.containsKey("roomKey")) {
            response.put("code", 400);
            response.put("message", "EnterRoom requires a roomKey!");
            return response;
        }

        String roomKey = (String)enterRoomParams.get("roomKey");

        RoomManager roomManager = RoomManager.getInstance();


        try {
            Room room = roomManager.getRoom(roomKey);
            room.playerJoinsRoom(key);

            var other = room.otherPlayer(key);
            if(other != null) {

            }
//            Map<String, Object> notification = new HashMap<>();
//            notification.put("event", "playerEntered");
//            notification.put("name", ((ClientInfo)other.attachment()).clientName);
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            CommunicationMaster.sendToClient((SocketChannel) other.channel(),
//                    objectMapper.writeValueAsString(notification));

        } catch (RoomDoesntExistsException e) {
            response.put("code", 400);
            response.put("message", "This key is invalid!");
            return response;
        } catch (PlayerAlreadyInRoomException e) {
            response.put("code", 400);
            response.put("message", "This player is already in this room!");
            return response;
        } catch (FullRoomException e) {
            response.put("code", 400);
            response.put("message", "This room is full!");
            return response;
        }



        response.put("code", 200);
        response.put("message","Successfully joined room!");
        return response;
    }
}
