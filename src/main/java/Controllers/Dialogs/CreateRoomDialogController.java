package Controllers.Dialogs;

import Models.Room;
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

import java.net.URL;
import java.util.ResourceBundle;

public class CreateRoomDialogController implements Initializable {

    @FXML
    private ComboBox<String> timePickerCombo;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;




    public void onCancel(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    public void onCreate(ActionEvent actionEvent) {
        if (username.getText().equals("") || password.getText().equals("") || timePickerCombo.getSelectionModel().getSelectedItem().equals(""))
            return;

        //create && start

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
