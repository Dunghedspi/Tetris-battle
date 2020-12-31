package com.dung.gui;

import com.google.common.eventbus.Subscribe;
import com.dung.app.GameController;
import com.dung.eventRequest.ExitRoomEvent;
import com.dung.eventRequest.MessageEvent;
import com.dung.eventRequest.StatusEvent;
import com.dung.eventResponse.AnotherClientResponse;
import com.dung.eventResponse.MessageResponseEvent;
import com.dung.eventResponse.RoomUpdateResponseEvent;
import com.dung.eventResponse.StatusResponseEvent;
import com.dung.lib.EventBusCustom;
import com.dung.logic.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RoomGuiController extends EventBusCustom implements Initializable {
	@FXML
	private VBox chatVBox;
	@FXML
	private Rectangle avatarClient1;
	@FXML
	private Rectangle avatarClient2;
	@FXML
	private Label userNameClient1;
	@FXML
	private Label userNameClient2;
	@FXML
	private Button roomTypeButton;
	@FXML
	private Button startGameButton;
	@FXML
	private TextField message;
	@FXML
	private Button sendMessButton;
	@FXML
	private Label roomId;
	@FXML
	private Button backButton;
	private Room room;
	public RoomGuiController () {
		attach(this);
	}
	public void handeClickButtonStartGame() {
		String text = startGameButton.getText();
		if (text.equals("Start")) {
			sendRequestStartGame("Start");
		} else if (text.equals("Ready")) {
			startGameButton.setText("OK");
			sendRequestStartGame("Yes");
		} else {
			startGameButton.setText("Ready");
			sendRequestStartGame("No");
		}

	}

	public void handeClickButtonBack(ActionEvent e) throws IOException {
		Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		URL location = getClass().getClassLoader().getResource("homeLayout.fxml");
		ResourceBundle resources = null;
		FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root, 410, 510);
		detach(this);
		primaryStage.setScene(scene);
		postEvent(new ExitRoomEvent());
	}

	public void handeClickSendMessButton(ActionEvent e) throws IOException {
		String mess = message.getText().trim();
		Label newMess = new Label(mess);
		newMess.setStyle("-fx-text-fill:yellow");
		chatVBox.getChildren().add(newMess);
		message.setText("");
		postEvent(new MessageEvent(mess));
	}

	public void setClient(String client1, String client2) {
		userNameClient1.setText(client1);
		userNameClient2.setText(client2);
	}

	public void setClient(String client1) {
		userNameClient1.setText(client1);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) throws NullPointerException {
		try {
			Image img = new Image("yasuo.jpg");
			avatarClient1.setFill(new ImagePattern(img));
			avatarClient2.setFill(new ImagePattern(img));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void setRoom(Room room) {
		this.room = room;
		roomId.setText(room.getRoomID());
		userNameClient1.setText(room.getUserNameClient1());
		userNameClient2.setText(room.getUserNameClient2());
		if (room.getMaster() == 1) {
			startGameButton.setDisable(room.getStatus() == 0);
		} else {
			startGameButton.setText("Ready");
		}
	}

	public void sendRequestStartGame(String status) {
		postEvent(new StatusEvent(status));
	}

	@Subscribe
	public void startGame(StatusResponseEvent statusResponseEvent) throws IOException {
		String status = statusResponseEvent.getStatus();
		if (status.equals("Start")) {
			Stage primaryStage = (Stage) message.getScene().getWindow();
			URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
			ResourceBundle resources = null;
			FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
			Parent root = fxmlLoader.load();
			GuiController c = fxmlLoader.getController();
			c.setRoom(room);
			Scene scene = new Scene(root, 410, 510);
			detach(this);
			primaryStage.setScene(scene);
			new GameController(c);
		} else startGameButton.setDisable(!status.equals("Yes"));
	}
	
	@Subscribe
	public void recvMessage(MessageResponseEvent messageResponseEvent){
		String mess = messageResponseEvent.getMess().trim();
		System.out.println(mess);
		Label newMess = new Label(mess);
		newMess.setStyle("-fx-text-fill:yellow");
		chatVBox.getChildren().add(newMess);
	}
	@Subscribe
	public void setAnotherClient(AnotherClientResponse anotherClientResponse){
		userNameClient2.setText(anotherClientResponse.getUsername());
	}
	@Subscribe
	public void UpdateRoom(RoomUpdateResponseEvent roomUpdateResponseEvent){
		this.setRoom(new Room(roomUpdateResponseEvent.getToken(),roomUpdateResponseEvent.getUsername1(), roomUpdateResponseEvent.getUsername2(),roomUpdateResponseEvent.getStatus(), roomUpdateResponseEvent.getMaster()));
		roomId.setText(room.getRoomID());
		userNameClient1.setText(room.getUserNameClient1());
		if (room.getMaster() == 1) {
			startGameButton.setDisable(room.getStatus() == 0);
			startGameButton.setText("Start");
		} else {
			startGameButton.setText("Ready");
		}
	}
}
