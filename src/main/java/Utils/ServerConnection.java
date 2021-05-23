package Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ServerConnection {

    private static final String address = "localhost";
    private static final int port = 4000;
    private static int MAX_MESSAGE_SIZE = 512;

    private static SocketChannel client;
    public static ObjectMapper objectMapper= new ObjectMapper();

    public static void connect() {
        try {
            client = SocketChannel.open(new InetSocketAddress(address, port));
            objectMapper = new ObjectMapper();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sendToServer(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_MESSAGE_SIZE);
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        client.write(buffer);
    }

    public static String getFromServer() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_MESSAGE_SIZE);
        client.read(buffer);
        return new String(buffer.array()).trim();
    }
}
