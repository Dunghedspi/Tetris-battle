package com.dung.socket;

import com.dung.eventRequest.ListBricks;
import com.dung.eventResponse.*;
import com.dung.lib.EventBusCustom;
import com.dung.network.Response;
import com.dung.network.StatusResponse;
import com.dung.utils.DefaultObjectTransmission;
import com.dung.utils.SerializableObjectAdapter;
import com.dung.utils.SocketByteTransmission;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

public class SocketListener extends EventBusCustom {
    Socket clientSocket;
    StatusResponse message;
    private final DefaultObjectTransmission transmission;
    public boolean isExit;

    public SocketListener(Socket c) throws IOException, InterruptedException {
        clientSocket = c;
        isExit = false;
        attach(this);
        transmission = new DefaultObjectTransmission(new SerializableObjectAdapter(), new SocketByteTransmission(c));
        SystemListener systemListener = new SystemListener(transmission);

        Runnable runnable = () -> {
            while (!isExit) {
                try {
                    Object receivedMessage = transmission.receiveObject();
                    if (receivedMessage instanceof Response) {
                        Platform.runLater(() -> handleResponse((Response) receivedMessage));
                        message = ((Response) receivedMessage).getStatus();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread newThread = new Thread(runnable);
        newThread.start();
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

    public void close() throws IOException {
        transmission.close();
        isExit = true;
    }

    public void handleResponse(Response response) {
        switch (response.getStatus()) {
            case OK: {
                postEvent(new LoginStatusResponseEvent("Ok"));
                break;
            }
            case ROOM: {
                String[] roomInfo = response.getData().split(",");
                postEvent(new RoomResponseEvent(roomInfo[1], roomInfo[2], roomInfo[0], Integer.parseInt(roomInfo[3]), Integer.parseInt(roomInfo[4]), Integer.parseInt(roomInfo[5])));
                break;
            }
            case MESS: {
                postEvent(new MessageResponseEvent(response.getData()));
                break;
            }
            case LIST: {
                postEvent(new ListBricks(response.getData()));
                break;
            }
            case ROWS: {
                postEvent(new RowsResponseEvent(response.getData()));
                break;
            }
            case START: {
                postEvent(new StatusResponseEvent("Start"));
                break;
            }
            case READY: {
                postEvent(new StatusResponseEvent("Yes"));
                break;
            }
            case REFUSE: {
                postEvent(new StatusResponseEvent("No"));
                break;
            }
            case UPDATE_ROOM: {
                String[] roomInfo = response.getData().split(",");
                postEvent(new RoomUpdateResponseEvent(roomInfo[1], roomInfo[2], roomInfo[0], Integer.parseInt(roomInfo[3]), Integer.parseInt(roomInfo[4]), Integer.parseInt(roomInfo[5])));
                break;
            }
            case JOIN_ROOM: {
                postEvent(new AnotherClientResponse(response.getData()));
                break;
            }
            case NOT_FOUND: {
                postEvent(new RoomNotFoundResponseEvent());
                break;
            }
            case WIN: {
                postEvent(new GameStatusResponse("win"));
                break;
            }
            case STATUS_ROOM:{
                postEvent(new StatusRoomResponse(response.getData()));
                break;
            }
        }
    }
}
