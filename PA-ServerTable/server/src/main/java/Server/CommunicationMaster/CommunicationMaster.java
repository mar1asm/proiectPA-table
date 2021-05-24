package Server.CommunicationMaster;

import Server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class CommunicationMaster {

    public static String readFromClient(SocketChannel client) throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        client.read(lengthBuffer);



        int size = ByteBuffer.wrap(lengthBuffer.array()).getInt(0);


        ByteBuffer buffer = ByteBuffer.allocate(size);
        client.read(buffer);
        return new String(buffer.array()).trim();
    }


    public static void sendToClient(SocketChannel client, String message) throws IOException {
        byte[] bytes = ByteBuffer.allocate(4).putInt(message.length()).array();
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.put(bytes);
        lengthBuffer.flip();
        client.write(lengthBuffer);

        ByteBuffer buffer = ByteBuffer.allocate(message.length());
        buffer.put(message.getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        client.write(buffer);
    }



}
