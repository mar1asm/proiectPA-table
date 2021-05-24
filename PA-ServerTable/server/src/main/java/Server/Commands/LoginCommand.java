package Server.Commands;

import Server.ClientInfo.ClientInfo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

public class LoginCommand implements Command{
    @Override
    public Object execute(Object... params) throws IOException {
        Map<String, Object> response = new HashMap<>();
        if(params.length < 2) {
            response.put("code", 500);
            response.put("message", "Internal server error!");
            return response;
        }

        if(params[0] == null) {
            response.put("code", 400);
            response.put("message","Missing parameters for login!");
            return response;
        }

        Map<String, Object> loginParams = (HashMap<String, Object>) params[0];
        SelectionKey key = (SelectionKey) params[1];

        if(!loginParams.containsKey("name")) {
            response.put("code", 401);
            response.put("message", "Wrong usage of Login! Requires <name> parameter");
            return response;
        }

        ClientInfo clientInfo = (ClientInfo) key.attachment();
        if(clientInfo.isLoggedOn) {
            response.put("code", 401);
            response.put("message", "User already logged on!");
            return response;
        }

        clientInfo.clientName = (String)loginParams.get("name");
        clientInfo.isLoggedOn = true;

        response.put("code", 200);
        //ToDo: aici o sa punem un token de logare cred... mega securitate :)))
        response.put("message", "Login successful!");
        return response;
    }
}
