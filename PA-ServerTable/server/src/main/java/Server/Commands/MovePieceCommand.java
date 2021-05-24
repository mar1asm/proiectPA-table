package Server.Commands;

import Server.ClientInfo.ClientInfo;
import Server.RoomManager.Exceptions.RoomDoesntExistsException;
import Server.RoomManager.Room.Room;
import Server.RoomManager.RoomManager;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

public class MovePieceCommand implements Command{

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

        if(!command.containsKey("from")) return null;

        if(!command.containsKey("to")) return null;

        ClientInfo clientInfo = (ClientInfo) key.attachment();

        String roomKey = clientInfo.roomKey;

        try {
            Room room = RoomManager.getInstance().getRoom(roomKey);
            int from = (Integer)command.get("from");
            int to = (Integer)command.get("to");
            int diceUsed = (Integer) command.get("diceUsed");
            room.MovePiece(from, to, diceUsed);
        } catch (RoomDoesntExistsException e) {
            e.printStackTrace();
        }

        return null;
    }
}
