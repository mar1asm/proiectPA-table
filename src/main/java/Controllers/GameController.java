package Controllers;

import Controllers.GameComponents.Piece;
import Controllers.GameComponents.PieceType;
import Controllers.GameComponents.PieseScoase;
import Controllers.GameComponents.Pip;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class GameController implements Initializable {

    public static int PIP_WIDTH;
    public static int PIP_HEIGHT;

    private final Group pieceGroup = new Group();
    private final Group pipGroup = new Group();

    private final List<Pip> pips = new ArrayList<>();


    private final Color dark = Color.valueOf("#e6c0ac");
    private final Color light = Color.valueOf("#70300d");


    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth() - 100;
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight() - 70;

    private static PieseScoase pieseScoaseAlb;
    private static PieseScoase pieseScoaseNegru;


    public  Pane createContent() {
        PIP_WIDTH = screenWidth / 13;
        PIP_HEIGHT = screenHeight / 2;
        Pane root = new Pane();
        root.setPrefSize(PIP_WIDTH * 13, PIP_HEIGHT * 2);
        root.getChildren().addAll(pipGroup, pieceGroup);

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
        }
        return root;
    }

    public void initGame() {


        for (int i = 0; i < 5; i++) {
            Piece piece = new Piece(PieceType.BLACK);
            pips.get(5).addPiece(piece);
        }

        for (int i = 0; i < 3; i++) {
            Piece piece = new Piece(PieceType.BLACK);
            pips.get(7).addPiece(piece);
        }

        for (int i = 0; i < 5; i++) {
            Piece piece = new Piece(PieceType.BLACK);
            pips.get(12).addPiece(piece);
        }

        for (int i = 0; i < 5; i++) {
            Piece piece = new Piece(PieceType.WHITE);
            pips.get(11).addPiece(piece);
        }

        for (int i = 0; i < 2; i++) {
            Piece piece = new Piece(PieceType.BLACK);
            pips.get(23).addPiece(piece);
        }

        for (int i = 0; i < 2; i++) {
            Piece piece = new Piece(PieceType.WHITE);
            pips.get(0).addPiece(piece);
        }

        for (int i = 0; i < 3; i++) {
            Piece piece = new Piece(PieceType.WHITE);
            pips.get(16).addPiece(piece);
        }

        for (int i = 0; i < 5; i++) {
            Piece piece = new Piece(PieceType.WHITE);
            pips.get(18).addPiece(piece);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
