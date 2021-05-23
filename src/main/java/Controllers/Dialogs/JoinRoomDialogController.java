package Controllers.Dialogs;

import Controllers.AppController;
import Controllers.GameController;
import Controllers.GameState;
import Models.RoomModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import static Models.RoomModel.joinRoom;

public class JoinRoomDialogController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;


    @FXML
    private TextField roomID;

    public void onCancel(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    public void setRoomID(String id) {
        roomID.setText(id);
    }

    public void onJoin(ActionEvent actionEvent) {
        if (roomID.getText().equals("") || username.getText().equals(""))
            return;
        try {
            AppController.showGameWindow();
            GameController.setGameParams("2", 2, roomID.getText());
            joinRoom(roomID.getText(), password.getText(), username.getText());
            GameState.setUsername(username.getText());
            GameState.setMyTurn(false);
            GameState.setMoveDirection(-1);
            // RoomModel.hideRoom(roomID.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //join && start
        closeWindow(actionEvent);
    }

    private void closeWindow(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.fireEvent(
                new WindowEvent(
                        stage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
    }
}
