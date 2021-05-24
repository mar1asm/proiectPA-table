package Main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RoomTest {
    private static final String address = "localHost";
    private static final int port = 4000;
    private static int MAX_MESSAGE_SIZE = 2048;

    public static void main(String[] args) {
        SocketChannel client;


        try {
            client  = SocketChannel.open(new InetSocketAddress(address, port));
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> loginRequest = new HashMap<>();
            Map<String, Object> loginParams = new HashMap<>();
            loginRequest.put("action", "Login");
            loginParams.put("name", "Stefan");

            loginRequest.put("params", loginParams);


            String loginRequestString = objectMapper.writeValueAsString(loginRequest);
            sendToServer(client, loginRequestString);


            String loginResponseString = readFromServer(client);

            System.out.println(loginResponseString);


            Map<String, Object> createRoomRequest = new HashMap<>();
            Map<String, Object> createRoomParams = new HashMap<>();
            createRoomParams.put("name", "Camera mea smechera");
            createRoomParams.put("time", 30);
            createRoomRequest.put("action", "CreateRoom");
            createRoomRequest.put("params", createRoomParams);
            String createRoomString = objectMapper.writeValueAsString(createRoomRequest);


            sendToServer(client, createRoomString);

            String createRoomResponseString = readFromServer(client);

            System.out.println(createRoomResponseString);


            Map<String, Object> getAllRoomsRequest = new HashMap<>();
            getAllRoomsRequest.put("action", "GetAllRooms");
            String getAllRoomsString = objectMapper.writeValueAsString(getAllRoomsRequest);

            sendToServer(client, getAllRoomsString);

            String getAllRoomsResponseString = readFromServer(client);

            System.out.println(getAllRoomsResponseString);


            ///Waiting 4 someone to enter

            String notification = readFromServer(client);

            Map<String, Object> startGameRequest = new HashMap<>();
            startGameRequest.put("action", "StartGame");

            String startGameString = objectMapper.writeValueAsString(startGameRequest);

            sendToServer(client, startGameString);

            String startGameResponseString = readFromServer(client);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromServer(SocketChannel client) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_MESSAGE_SIZE);
        client.read(buffer);
        return new String(buffer.array()).trim();
    }

    public static void sendToServer(SocketChannel client, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_MESSAGE_SIZE);
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        client.write(buffer);
    }
}
