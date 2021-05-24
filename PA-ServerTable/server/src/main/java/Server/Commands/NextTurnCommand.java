package Server.Commands;

import Server.ClientInfo.ClientInfo;
import Server.RoomManager.Exceptions.RoomDoesntExistsException;
import Server.RoomManager.Room.Room;
import Server.RoomManager.RoomManager;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

public class NextTurnCommand implements Command{
    @Override
    public Object execute(Object... params) throws IOException {
        SelectionKey key = (SelectionKey) params[1];
        ClientInfo clientInfo = (ClientInfo) key.attachment();

        try {
            Room room = RoomManager.getInstance().getRoom(clientInfo.roomKey);
            room.NextTurn();
        } catch (RoomDoesntExistsException e) {
            e.printStackTrace();
        }

        return null;
    }
}
