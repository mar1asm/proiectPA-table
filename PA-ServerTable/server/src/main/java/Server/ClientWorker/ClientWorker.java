package Server.ClientWorker;

import Server.ClientInfo.ClientInfo;
import Server.Commands.Command;
import Server.CommunicationMaster.CommunicationMaster;
import Server.Server;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ClientWorker implements Runnable{


    private SelectionKey key;

    public ClientWorker(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {

        SocketChannel client = (SocketChannel) key.channel();
        ObjectMapper objectMapper = new ObjectMapper();

        try {


            String requestString = CommunicationMaster.readFromClient(client);

            ClientInfo clientInfo = (ClientInfo) key.attachment();
            String name = "Unregistered user";
            if(clientInfo.isLoggedOn) {
                name = clientInfo.clientName;
            }
            System.out.println(name + ": " + requestString);

            Map<String, Object> request = objectMapper.readValue(requestString, HashMap.class);

            var response = handleRequest(request);

            if(response == null) return;

            String responseString = objectMapper.writeValueAsString(response);

            CommunicationMaster.sendToClient(client, responseString);



        } catch (JsonParseException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 401);
            response.put("message", "Bad format!");
            String responseString = null;
            try {
                responseString = objectMapper.writeValueAsString(response);
                CommunicationMaster.sendToClient(client, responseString);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            e.printStackTrace();
        }
        catch (IOException e) {
            //connection reset cel mai probabil
            try {
                client.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
            return;
        }
        finally {
            if(key.isValid())
                key.interestOps(SelectionKey.OP_READ);
        }


    }



    private Map<String, Object> handleRequest(Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        if(!request.containsKey("action")) {
            //nu stiu codurile.... :)))))
            response.put("code", 405);
            response.put("message", "Missing the action !");
            return response;
        }

//        if(!request.containsKey("params")) {
//            //nu stiu codurile.... :)))))
//            response.put("code", 405);
//            response.put("message", "Parameters are missing!");
//            return response;
//        }


        String commandName = (String)request.get("action");
        try {
            String className = "Server.Commands." + commandName + "Command";
            Class clazz = Class.forName(className);
            Command command = (Command) clazz.getConstructor().newInstance();
            Object params = request.get("params");
            response = (HashMap)command.execute(params, key);
            return response;
        } catch (ClassNotFoundException e) {
            response.put("code", "404");
            response.put("message", "Method " + commandName + " doesn't exists");
            return response;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
