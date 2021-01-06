package com.dung.gui;

import com.dung.eventResponse.RoomNotFoundResponseEvent;
import com.google.common.eventbus.Subscribe;
import com.dung.eventRequest.RoomTypeEvent;
import com.dung.eventResponse.RoomResponseEvent;
import com.dung.lib.EventBusCustom;
import com.dung.logic.Room;
import com.dung.logic.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeGuiController extends EventBusCustom implements Initializable {
	@FXML
	private Button createGame;
	@FXML
	private Button findGame;
	@FXML
	private Button Join;
	@FXML
	private TextField tokenRoom;
	@FXML
	private Button Back;
	@FXML
	private Label username;
	@FXML
	private Circle createIcon, publicIcon, privateIcon;
	private String roomType;
	public HomeGuiController(){
		attach(this);
	}
	public void handeClickButtonCreateGame(ActionEvent e) throws IOException {
		this.roomType = "create";
		sendRoomType();
	}

	public void handeClickButtonPublic(ActionEvent e) throws IOException {
		this.roomType = "public";
		sendRoomType();
	}

	public void handeClickButtonPrivate(ActionEvent e) throws IOException {
		this.roomType = "private";
		if (tokenRoom != null) {
			System.out.println(tokenRoom.getText());
			sendRoomType();
		}

	}

	public void handeClickButtonBack(ActionEvent e) throws IOException {
		Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		URL location = getClass().getClassLoader().getResource("startLayout.fxml");
		ResourceBundle resources = null;
		FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root, 410, 510);
		detach(this);
		primaryStage.setScene(scene);
	}

	public void setUser(User user) {
		username.setText(user.getUsername());
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Image iconCreate = new Image("icon.jpeg");
			ImagePattern iconPattern = new ImagePattern(iconCreate);
			createIcon.setFill(iconPattern);
			publicIcon.setFill(iconPattern);
			privateIcon.setFill(iconPattern);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void sendRoomType() {
		if (tokenRoom != null) {
			postEvent(new RoomTypeEvent(this.roomType, tokenRoom.getText()));
		} else {
			postEvent(new RoomTypeEvent(this.roomType, ""));
		}
	}
	@Subscribe
	public void joinRoom(RoomResponseEvent roomResponseEvent) throws IOException {
		Stage primaryStage = (Stage) username.getScene().getWindow();
		URL location = getClass().getClassLoader().getResource("roomLayout.fxml");
		ResourceBundle resources = null;
		FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
		Parent root = fxmlLoader.load();
		RoomGuiController c = fxmlLoader.getController();
		Scene scene = new Scene(root, 410, 510);
		c.setRoom(new Room(roomResponseEvent.getToken(),roomResponseEvent.getUsername1(), roomResponseEvent.getUsername2(),roomResponseEvent.getStatus(), roomResponseEvent.getMaster(), roomResponseEvent.getIsPublic()));
		detach(this);
		primaryStage.setScene(scene);
	}
	@Subscribe
	public void RoomNotFound(RoomNotFoundResponseEvent roomNotFoundResponseEvent){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText("Sorry!Can't find the room!");
		alert.show();
	}
}