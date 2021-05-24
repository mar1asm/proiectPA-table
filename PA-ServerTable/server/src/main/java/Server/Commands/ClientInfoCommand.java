package Server.Commands;

import Server.ClientInfo.ClientInfo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

public class ClientInfoCommand implements Command{

    @Override
    public Object execute(Object... params) throws IOException {
        Map<String, Object> response = new HashMap<>();
        if(params.length < 2) {
            response.put("code", 500);
            response.put("message", "Internal server error!");
            return response;
        }
        SelectionKey key = (SelectionKey) params[1];


        ClientInfo clientInfo = (ClientInfo) key.attachment();


        response.put("code", "200");
        if(clientInfo.isLoggedOn) {
            response.put("username", clientInfo.clientName);
        }
        else {
            response.put("usename", "N/A");
        }

        return response;
    }
}
