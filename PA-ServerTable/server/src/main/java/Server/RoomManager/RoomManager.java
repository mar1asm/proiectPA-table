package Server.RoomManager;

import Server.RoomManager.Exceptions.RoomDoesntExistsException;
import Server.RoomManager.Room.Room;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RoomManager {
    private static final int KEY_LENGTH = 8;
    private static RoomManager instance;

    private Map<String, Room> rooms = new HashMap<>();

    private RoomManager() {

    }



    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public void removeRoom(String key) {
        rooms.remove(key);
    }

    public boolean roomExists(String key) {
        return rooms.containsKey(key);
    }

    public Room getRoom(String key) throws RoomDoesntExistsException {
        if (!roomExists(key)) throw new RoomDoesntExistsException("The room with the key " + key + " doesn't exists!");
        return rooms.get(key);
    }

    public List<Room> getAllRooms() {
        return new LinkedList<>(rooms.values());
    }


    private String generateKey() {
        StringBuilder sb = new StringBuilder(KEY_LENGTH);
        String availableTokens = "abcdefghhijklmnopqrstuvwxyz01235456879ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        Random random = new Random();
        for (int i = 0; i < KEY_LENGTH; ++i) {
            int index = random.nextInt(availableTokens.length());
            sb.append((availableTokens.charAt(index)));
        }
        return sb.toString();
    }

    public String createNewRoom(SelectionKey creator, String name, int time) {
        String key;
        do {
            key = generateKey();
        } while (roomExists(key));

        Room room = new Room(creator, name, key, time);

        rooms.put(key, room);

        return key;
    }
}
