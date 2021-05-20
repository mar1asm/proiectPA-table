package Models;

public class Room {
    private String id;
    private String name;
    private String host;
    private int timePerMove;

    public Room(String id, String name, String host, int timePerMove) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.timePerMove = timePerMove;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getTimePerMove() {
        return timePerMove;
    }

    public void setTimePerMove(int timePerMove) {
        this.timePerMove = timePerMove;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", timePerMove=" + timePerMove +
                '}';
    }
}
