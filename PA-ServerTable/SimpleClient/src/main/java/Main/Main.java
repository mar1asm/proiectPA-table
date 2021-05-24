package Main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String address = "localHost";
    private static final int port = 4000;
    private static int MAX_MESSAGE_SIZE = 512;

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
            var loginResponse = objectMapper.readValue(loginResponseString, HashMap.class);

            while(true) {
                Map<String, Object> request = new HashMap<>();

                request.put("action", "ClientInfo");
                request.put("params", "parametrii uau");

                String requestString = objectMapper.writeValueAsString(request);

                sendToServer(client, requestString);

                String responseString = readFromServer(client);

                System.out.println(responseString);
//                String message = "ana are mere";
//                ByteBuffer buffer = ByteBuffer.allocate(512);
//                buffer.put(message.getBytes(StandardCharsets.UTF_8));
//                buffer.flip();
//                client.write(buffer);
//
//                System.out.println("Trimis-am!");
//
//                buffer.flip();
//                buffer.clear();
//                client.read(buffer);
//
//
//                String response = new String(buffer.array()).trim();
//                System.out.println("Serverul a zis : " + response);

                TimeUnit.SECONDS.sleep(2);
            }
        } catch (IOException | InterruptedException e) {
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

