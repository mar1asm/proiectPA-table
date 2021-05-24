package Main;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RoomTest2 {
    private static final String address = "localHost";
    private static final int port = 4000;
    private static int MAX_MESSAGE_SIZE = 2048;
    private static String roomKey = "0ToulbHy";

    public static void main(String[] args) {
        SocketChannel client;


        try {
            client  = SocketChannel.open(new InetSocketAddress(address, port));
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> loginRequest = new HashMap<>();
            Map<String, Object> loginParams = new HashMap<>();
            loginRequest.put("action", "Login");
            loginParams.put("name", "Maria");
            loginRequest.put("params", loginParams);

            String loginRequestString = objectMapper.writeValueAsString(loginRequest);
            sendToServer(client, loginRequestString);


            String loginResponseString = readFromServer(client);

            System.out.println(loginResponseString);


            Map<String, Object> enterRoomRequest = new HashMap<>();
            Map<String, Object> enterRoomParams = new HashMap<>();
            enterRoomParams.put("roomKey", roomKey);

            enterRoomRequest.put("action", "EnterRoom");
            enterRoomRequest.put("params", enterRoomParams);

            String enterRoomString = objectMapper.writeValueAsString(enterRoomRequest);


            sendToServer(client, enterRoomString);


            String enterRoomResponseString = readFromServer(client);

            System.out.println(enterRoomResponseString);

            String notification = readFromServer(client);

            System.out.println(notification);

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
