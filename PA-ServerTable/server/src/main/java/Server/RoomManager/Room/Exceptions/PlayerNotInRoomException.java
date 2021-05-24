package Server.RoomManager.Room.Exceptions;

public class PlayerNotInRoomException extends Exception{
    public PlayerNotInRoomException(String message) {
        super(message);
    }
}
