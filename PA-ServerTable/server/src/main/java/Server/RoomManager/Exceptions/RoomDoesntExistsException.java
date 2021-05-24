package Server.RoomManager.Exceptions;

public class RoomDoesntExistsException extends Exception{
    public RoomDoesntExistsException(String message) {
        super(message);
    }
}
