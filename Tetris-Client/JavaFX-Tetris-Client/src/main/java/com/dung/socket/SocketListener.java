package com.dung.socket;

import com.dung.eventRequest.ListBricks;
import com.dung.eventRequest.LoginEvent;
import com.dung.eventResponse.*;
import com.dung.lib.EventBusCustom;
import com.dung.network.Request;
import com.dung.network.Response;
import com.dung.network.StatusResponse;
import com.dung.utils.DefaultObjectTransmission;
import com.dung.utils.SerializableObjectAdapter;
import com.dung.utils.SocketByteTransmission;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

public class SocketListener extends EventBusCustom {
    Socket clientSocket;
    StatusResponse message;
    private final DefaultObjectTransmission transmission;
    private SystemListener systemListener;
    Thread serverListThread;

    public Thread getServerListThread() {
        return serverListThread;
    }

    public SocketListener(Socket c) throws IOException {
        clientSocket = c;
        attach(this);
        transmission = new DefaultObjectTransmission(new SerializableObjectAdapter(), new SocketByteTransmission(c));
        SystemListener systemListener = new SystemListener(transmission);
        serverListThread = new Thread() {
            @Override
            public void run() {
                try {
                    do {
                        Object receivedMessage = transmission.receiveObject();
                        if (receivedMessage instanceof Response) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    handleResponse((Response) receivedMessage);
                                }
                            });
                            message = ((Response) receivedMessage).getStatus();
                        }
                    } while (message != StatusResponse.QUIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serverListThread.start();
    }
    public void close() throws IOException {
        System.out.println(1);
        transmission.close();
        serverListThread.interrupt();
    }

    public void handleResponse(Response response) {
        switch (response.getStatus()) {
            case OK: {
                postEvent(new LoginStatusResponseEvent("Ok"));
                break;
            }
            case ROOM: {
                String[] roomInfo = response.getData().split(",");
                postEvent(new RoomResponseEvent(roomInfo[1], roomInfo[2], roomInfo[0], Integer.parseInt(roomInfo[3]), Integer.parseInt(roomInfo[4])));
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
                postEvent(new RoomUpdateResponseEvent(roomInfo[1], roomInfo[2], roomInfo[0], Integer.parseInt(roomInfo[3]), Integer.parseInt(roomInfo[4])));
                break;
            }
            case JOIN_ROOM: {
                postEvent(new AnotherClientResponse(response.getData()));
                break;
            }
            case WIN: {
                postEvent(new GameStatusResponse("win"));
                break;
            }
        }
    }
}
