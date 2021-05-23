package Controllers.Dialogs;

import Controllers.AppController;
import Controllers.GameController;
import Models.Room;
import Models.RoomModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateRoomDialogController implements Initializable {

    @FXML
    private ComboBox<String> timePickerCombo;

    @FXML
    private TextField username;

    @FXML
    private TextField roomName;

    @FXML
    private PasswordField password;




    public void onCancel(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    public void onCreate(ActionEvent actionEvent) {
        if (username.getText().equals("") || password.getText().equals("") || roomName.getText().equals("") || timePickerCombo.getSelectionModel().getSelectedItem().equals(""))
            return;

        try {
            RoomModel.createRoom(username.getText(), password.getText(), roomName.getText(),Integer.parseInt(timePickerCombo.getSelectionModel().getSelectedItem() ));
            AppController.showGameWindow();
            //GameController.setGameParams("1", 1, "5");
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timePickerCombo.getItems().add("30");
        timePickerCombo.getItems().add("40");
        timePickerCombo.getItems().add("50");
    }
}
