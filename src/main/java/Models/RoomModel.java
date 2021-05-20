package Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomModel {
    //get from db

    public static List<Room> getAll(){
        return Arrays.asList(new Room ("1", "nume camera...","maria", 20), new Room("2","nume camera amuzant...", "stefan", 30));
    }

}
