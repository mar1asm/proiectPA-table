package Controllers;

import Controllers.GameComponents.*;
import Utils.ServerListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static Utils.ServerConnection.objectMapper;
import static javafx.application.Platform.runLater;


public class GameController implements Initializable {
    static Player player;
    static GameState gameState;

    MoveController moveController;

    public GameController() {
    }

    public static void setGameParams(String playerID, int playerType, String roomID) {
        player = new Player(playerID, playerType);
        gameState = new GameState();
        gameState.setGameID(roomID);
        waitForGameStart();
    }

    public static int PIP_WIDTH;
    public static int PIP_HEIGHT;

    private final Group pieceGroup = new Group();
    private final Group pipGroup = new Group();

    private static List<Pip> pips = new ArrayList<>();


    private final Color dark = Color.valueOf("#e6c0ac");
    private final Color light = Color.valueOf("#70300d");


    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth() - 100;
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight() - 70;

    public static PieseScoase pieseScoaseAlb;
    public static PieseScoase pieseScoaseNegru;


    public Pane createContent() {

        PIP_WIDTH = screenWidth / 13;
        PIP_HEIGHT = screenHeight / 2;


        Pane root = new Pane();
        root.setPrefSize(PIP_WIDTH * 13, PIP_HEIGHT * 2);
        root.getChildren().addAll(pipGroup, pieceGroup);

        //Header h=new Header(100, screenWidth);
        //root.getChildren().add(h);

        pieseScoaseAlb = new PieseScoase(6 * PIP_WIDTH, 0);
        pieseScoaseNegru = new PieseScoase(6 * PIP_WIDTH, PIP_HEIGHT);
        pieseScoaseNegru.setRotate(180);

        root.getChildren().add(pieseScoaseAlb);
        root.getChildren().add(pieseScoaseNegru);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 13; j++) {
                if (j == 6) {
                    continue;
                }
                Color color;

                if (i == 0) {

                    if (j % 2 == 0)
                        color = dark;
                    else
                        color = light;
                } else {
                    if (j % 2 == 0)
                        color = light;
                    else
                        color = dark;
                }
                int coordX = i == 0 ? (12 - j) * PIP_WIDTH : j * PIP_WIDTH;


                Pip pip = new Pip(pips.size(), color, coordX, i * PIP_HEIGHT, i == 1);

                pips.add(pip);
                pipGroup.getChildren().add(pip);
            }
            moveController = new MoveController(pips);
        }
        return root;
    }


    public static void updateBoard(String config) {
        try {
            var response = objectMapper.readValue(config, HashMap.class);
            var gameState = (HashMap<String, Object>) response.get("gameState");

            for (int i = 0; i < 24; i++) {
                Piece piece = pips.get(i).movePiece();
                while (piece != null) {
                    piece.dissapear();
                    piece = pips.get(i).movePiece();
                }
            }
            for (int i = 0; i < 24; i++) {
                String index = String.valueOf(i);
                var pip = (HashMap<String, Object>) gameState.get(index);
                int nbPieces = (Integer) pip.get("nb");
                if (nbPieces != 0) {
                    for (int j = 0; j < nbPieces; j++) {
                        Piece piece = new Piece(pip.get("type").equals("WHITE") ? PieceType.WHITE : PieceType.BLACK);

                        int finalI = i;
                        runLater(() -> pips.get(finalI).addPiece(piece));
                    }
                }

            }
            while (pieseScoaseNegru.getPieces().size()>0)
                pieseScoaseNegru.movePiece().dissapear();
            while (pieseScoaseAlb.getPieces().size()>0)
                pieseScoaseAlb.movePiece().dissapear();

            for (int j=0; j<(Integer) gameState.get("removedBlack"); j++)
                pieseScoaseNegru.addPiece(new Piece (PieceType.BLACK));
            for (int j=0; j<(Integer) gameState.get("removedWhite"); j++)
                pieseScoaseAlb.addPiece(new Piece (PieceType.WHITE));


//            if(GameState.isMyTurn()) {
//                MoveController.highlightPieces(true);
//            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static PieseScoase getPieseScoase(int type) {
        return type == 1 ? pieseScoaseAlb : pieseScoaseNegru;
    }

    public static void waitForGameStart() {
        // ?????????????????????? nujt
        System.out.println(gameState.getGameID());
    }


    public void waitForMove() {
        //
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread listener = new Thread(new ServerListener());
        listener.start();
    }
}
