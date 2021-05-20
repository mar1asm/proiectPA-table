package Controllers;

import Controllers.Dialogs.JoinRoomDialogController;
import Models.Room;
import Models.RoomModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class OnlineViewController implements Initializable {

    @FXML
    private TableView<Room> tableView;
    @FXML
    private TableColumn<Room, String> id;
    @FXML
    private TableColumn<Room, String> host;
    @FXML
    private TableColumn<Room, Integer> time;

    private ObservableList<Room> data;

    public void onJoinRoom(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dialogs/JoinRoomDialog.fxml"));
        Room room = tableView.getSelectionModel().getSelectedItem();
        //System.out.println(room);
        LoadStage(loader);
        try {
            String roomID = tableView.getSelectionModel().getSelectedItem().getId();

            JoinRoomDialogController joinRoomDialogController = loader.getController();
            joinRoomDialogController.setRoomID(roomID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LoadStage(FXMLLoader loader) {
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Create room");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void onCreateRoom(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dialogs/CreateRoomDialog.fxml"));
        LoadStage(loader);


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        host.setCellValueFactory(new PropertyValueFactory<>("host"));
        time.setCellValueFactory(new PropertyValueFactory<>("timePerMove"));
        data = FXCollections.observableArrayList(RoomModel.getAll());
        tableView.setItems(data);
    }
}
