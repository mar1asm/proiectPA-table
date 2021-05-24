package Server.Commands;

import java.io.IOException;
import java.util.HashMap;

public class TestCommand  implements Command{

    @Override
    public Object execute(Object... params) throws IOException {

        HashMap<String, Object> response = new HashMap<>();

        response.put("code", 200);

        return response;
    }
}
