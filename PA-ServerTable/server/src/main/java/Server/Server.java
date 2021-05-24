package Server;

import Server.ClientInfo.ClientInfo;
import Server.ClientWorker.ClientWorker;
import Server.ThreadPool.ThreadPool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class Server {
    private static final String address = "localHost";
    private static final int port = 4000;
    private static boolean isRunning = false;
    public static int MAX_MESSAGE_SIZE = 2048;
    private static ThreadPool workers = new ThreadPool(10);

    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(address, port));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            isRunning = true;
            System.out.println("Waiting for clients...");
            while(isRunning) {
                selector.select(1000);
                var selectedKeys = selector.selectedKeys();

                var iter = selectedKeys.iterator();
                while(iter.hasNext()) {
                    var key = (SelectionKey)iter.next();
                    iter.remove();
                    if(key.isAcceptable()) {
                        register(selector, serverSocket);
                    }

                    if(key.isReadable()) {
                        key.interestOps(0);
                        System.out.println("baca bre la citit");
                        //key.interestOps(SelectionKey.OP_READ);
                        workers.execute(new ClientWorker(key));
                    }
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        if(client == null) return;
        client.configureBlocking(false);
        var key = client.register(selector, SelectionKey.OP_READ);
        key.attach(new ClientInfo());
        System.out.println("Bun venit!");
    }

    public static synchronized void register(Selector selector, SocketChannel channel) throws IOException {
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        System.out.println("ADAUGAAAT");
    }

    private static void clientWork(SelectionKey key) throws IOException {

    }
}
