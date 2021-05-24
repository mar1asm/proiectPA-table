package Server.Commands;

import java.io.IOException;

public interface Command {
    Object execute(Object ...params) throws IOException;
}
