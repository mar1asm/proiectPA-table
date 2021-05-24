package Server.RoomManager.Room;

import Server.ClientInfo.ClientInfo;
import Server.Commands.ClientInfoCommand;
import Server.CommunicationMaster.CommunicationMaster;
import Server.RoomManager.BackgammonGameState;
import Server.RoomManager.Room.Exceptions.FullRoomException;
import Server.RoomManager.Room.Exceptions.PlayerAlreadyInRoomException;
import Server.RoomManager.Room.Exceptions.PlayerNotInRoomException;
import Server.RoomManager.RoomManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Room {
    private String key;
    private String name;
    private SelectionKey player1;
    private SelectionKey player2;
    private BackgammonGameState gameState;
    private int time;
    private boolean inGame = false;

    public Room(SelectionKey gameCreator, String name) {
        this.player1 = gameCreator;
        gameState = new BackgammonGameState();
        this.name = name;
    }

    public Room(SelectionKey gameCreator, String name, String key) {
        this.player1 = gameCreator;
        gameState = new BackgammonGameState();
        ((ClientInfo)gameCreator.attachment()).inRoom = true;
        ((ClientInfo)gameCreator.attachment()).roomKey = key;
        this.name = name;
        this.key = key;
    }


    public Room(SelectionKey gameCreator, String name, String key, int time) {
        this.player1 = gameCreator;
        gameState = new BackgammonGameState();
        ((ClientInfo)gameCreator.attachment()).inRoom = true;
        ((ClientInfo)gameCreator.attachment()).roomKey = key;
        this.name = name;
        this.key = key;
        this.time = time;
    }

    public Room(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean getStarted() {
        return inGame;
    }

    public void sendToClients(String message) throws IOException {
        CommunicationMaster.sendToClient((SocketChannel)player1.channel(), message);
        CommunicationMaster.sendToClient((SocketChannel)player2.channel(), message);
    }

    public void startGame() {
        inGame = true;
        gameState.initGame();


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SendUpdatedBoardToClients();

            Map<String, Object> turnNotification = new HashMap<>();

            turnNotification.put("action", "turn");
            ClientInfo clientInfo = new ClientInfo();
            if(gameState.getTurn() == 0) {
                clientInfo = (ClientInfo)player1.attachment();
            }
            else {
                clientInfo = (ClientInfo)player2.attachment();
            }

            turnNotification.put("player", clientInfo.clientName);
            Random random = new Random();
            turnNotification.put("die1", gameState.dice1);
            turnNotification.put("die2", gameState.dice2);
//            turnNotification.put("die1", 1);
//            turnNotification.put("die2", 1);

            String turnNotificationString = objectMapper.writeValueAsString(turnNotification);

            sendToClients(turnNotificationString);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendUpdatedBoardToClients() {
        Map<String, Object> startGameNotification =  new HashMap<>();

        startGameNotification.put("action", "updateBoard");
        startGameNotification.put("gameState", gameState.serializeToJson());

        startGameNotification.put("currentPlayer", getNameOfCurrentPlayer());

        startGameNotification.put("canMove", gameState.checkPossibilityToMove());

        ObjectMapper objectMapper = new ObjectMapper();

        String startGameNotificationString = null;
        try {
            startGameNotificationString = objectMapper.writeValueAsString(startGameNotification);
            System.out.println("sending " + startGameNotificationString);
            sendToClients(startGameNotificationString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void MovePiece(int pip1index, int pip2index, int diceUsed) {
        gameState.MovePiece(pip1index, pip2index);
        gameState.removeDice(diceUsed);
        SendUpdatedBoardToClients();
    }

    public String getNameOfCurrentPlayer() {
        ClientInfo clientInfo = new ClientInfo();
        if(gameState.getTurn() == 0) {
            clientInfo = (ClientInfo)player1.attachment();
        }
        else {
            clientInfo = (ClientInfo)player2.attachment();
        }
        return clientInfo.clientName;
    }

    public void NextTurn() throws IOException{
        gameState.nextTurn();
        Map<String, Object> turnNotification = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        turnNotification.put("action", "turn");

        String clientName = getNameOfCurrentPlayer();

        turnNotification.put("player", clientName);
        Random random = new Random();
        turnNotification.put("die1", gameState.dice1);
        turnNotification.put("die2", gameState.dice2);
//        turnNotification.put("die1", 1);
//        turnNotification.put("die2", 1);

        String turnNotificationString = objectMapper.writeValueAsString(turnNotification);

        sendToClients(turnNotificationString);

        SendUpdatedBoardToClients();
    }

    //trebuie sa fac mai ciudat asa ca sa nu apara dupa in json cand trimitem..
    public boolean isHost(SelectionKey player) {
        return player1 == player;
    }

    public int getTime() {
        return time;
    }

    public String getHostName() {
        return ((ClientInfo)player1.attachment()).clientName;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public boolean full() {
        return player1 != null && player2 != null;
    }

    public SelectionKey otherPlayer(SelectionKey player) {
        if(player == player1) return player2;
        if(player == player2) return player1;
        return null;
    }

    public void playerJoinsRoom(SelectionKey player) throws FullRoomException, PlayerAlreadyInRoomException {
        if(full()) {
            throw new FullRoomException("Room " + name + " is full!");
        }

        ClientInfo playerInfo = (ClientInfo)player.attachment();

        if(player1 == null) {
            if(player2 == player) {
                String playerName = playerInfo.clientName;
                throw new PlayerAlreadyInRoomException("Player " + playerName +  " is already in the room!");
            }
            player1 = player;
            playerInfo.inRoom = true;
            playerInfo.roomKey = key;
            return;
        }

        if(player1 == player) {
            String playerName = playerInfo.clientName;
            throw new PlayerAlreadyInRoomException("Player " + playerName +  " is already in the room!");
        }

        playerInfo.inRoom = true;
        playerInfo.roomKey = key;
        player2 = player;

        startGame();
    }

    public int numberOfPlayers() {
        int count = 0;
        if(player1 != null) count++;
        if(player2 != null) count++;
        return  count;
    }


    private void checkRoomValidity() {
        if(player1 == null && player2 == null) {
            RoomManager.getInstance().removeRoom(key);
        }
    }

    public void playerLeavesRoom(SelectionKey player) throws PlayerNotInRoomException {
        ClientInfo playerInfo = (ClientInfo) player.attachment();
        if(player1 == player) {
            player1 = player2;
            player2 = null;
            playerInfo.inRoom = false;

        }
        if(player2 == player) {
            player2 = null;
            playerInfo.inRoom = false;
        }

        throw new PlayerNotInRoomException("Player " + playerInfo.clientName + " is not in this room!");
    }

}
