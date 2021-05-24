package Server.Commands;

import Server.RoomManager.RoomManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetAllRoomsCommand implements Command{
    @Override
    public Object execute(Object... params) throws IOException {
        var rooms = RoomManager.getInstance().getAllRooms();

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);

        response.put("rooms", rooms);

        return response;
    }
}
