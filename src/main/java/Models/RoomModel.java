package Models;


import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static Models.Commands.login;
import static Utils.ServerConnection.getFromServer;
import static Utils.ServerConnection.sendToServer;
import static Utils.ServerConnection.objectMapper;

public class RoomModel {

    public static List<Room> getAll() {
        Map<String, Object> getAllRoomsRequest = new HashMap<>();
        getAllRoomsRequest.put("action", "GetAllRooms");
        Map<String, Object> response;
        try {
            String getAllRoomsString = objectMapper.writeValueAsString(getAllRoomsRequest);

            sendToServer(getAllRoomsString);

            String getAllRoomsResponseString = getFromServer();
            response = objectMapper.readValue(getAllRoomsResponseString, HashMap.class);
            var rooms = (List<HashMap<String, Object>>) response.get("rooms");
            List<Room> roomList = new ArrayList<>();
            System.out.println(getAllRoomsResponseString);
            for (var room : rooms) {
                Room newRoom = new Room(room.get("key").toString(), room.get("name").toString(), room.get("hostName").toString(), room.get("started").equals("true"), Integer.parseInt( room.get("time").toString() ));
                roomList.add(newRoom);
            }
            return roomList;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Room> getAvailableRooms(){
        List<Room> rooms = getAll();
        List<Room> availableRooms = rooms.stream().filter(room -> !room.isStarted()).collect(Collectors.toList());
        return availableRooms;
    }

    public static boolean createRoom(String hostName, String password, String roomName, int time) throws IOException {
        login(hostName);

        Map<String, Object> createRoomRequest = new HashMap<>();
        Map<String, Object> createRoomParams = new HashMap<>();
        createRoomParams.put("name", roomName);
        createRoomParams.put("password", password);
        createRoomParams.put("time", time);

        createRoomRequest.put("action", "CreateRoom");
        createRoomRequest.put("params", createRoomParams);
        String createRoomString = objectMapper.writeValueAsString(createRoomRequest);


        sendToServer(createRoomString);

        String createRoomResponseString = getFromServer();

        System.out.println(createRoomResponseString);

        return true;
    }

    public static void joinRoom(String id, String password, String username) throws IOException {
        login(username);
        Map<String, Object> joinRoomRequest = new HashMap<>();
        Map<String, Object> joinRoomParams = new HashMap<>();
        joinRoomParams.put("roomKey", id);

        joinRoomRequest.put("action", "EnterRoom");
        joinRoomRequest.put("params", joinRoomParams);
        String createRoomString = objectMapper.writeValueAsString(joinRoomRequest);


        sendToServer(createRoomString);
    }

   // public static void deleteRoom(String id) throws IOException {
   //     Map<String, Object> deleteRoomRequest = new HashMap<>();
   //     Map<String, Object> deleteRoomParams = new HashMap<>();
   //     deleteRoomParams.put("roomID", id);
//
   //     deleteRoomRequest.put("action", "deleteRoom");
   //     deleteRoomRequest.put("params", deleteRoomParams);
   //     String createRoomString = objectMapper.writeValueAsString(deleteRoomRequest);
//
//
   //     sendToServer(createRoomString);
//
   //     String createRoomResponseString = getFromServer();
//
   //     System.out.println(createRoomResponseString);
   // }


}
