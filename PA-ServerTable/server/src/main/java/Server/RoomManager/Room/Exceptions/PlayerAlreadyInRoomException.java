package Server.RoomManager.Room.Exceptions;

public class PlayerAlreadyInRoomException extends Exception{
    public PlayerAlreadyInRoomException(String message) {
        super(message);
    }
}
