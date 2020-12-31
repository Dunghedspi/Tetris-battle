package com.dung.socket;

import com.dung.lib.EventBusCustom;
import com.dung.eventRequest.*;
import com.dung.network.Request;
import com.dung.utils.DefaultObjectTransmission;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;

public class SystemListener extends EventBusCustom {
    DefaultObjectTransmission transmission;
    public SystemListener(DefaultObjectTransmission transmission){
        this.transmission = transmission;
        attach(this);
    }

    @Subscribe
    public void sendStatusGame(GameStatusEvent gameStatusEvent){
        sendRequest("lose","");
    }
    @Subscribe
    public void login(LoginEvent loginEvent){
        sendRequest("user", loginEvent.getUsername());
    }
    @Subscribe
    public void joinRoomType(RoomTypeEvent roomTypeEvent){
        sendRequest("type", roomTypeEvent.getTypeRoom());
    }
    @Subscribe
    public void sendMess(MessageEvent messageEvent){
        sendRequest("mess",messageEvent.getMessage());
    }
    @Subscribe
    public void exitRoom(ExitRoomEvent exitRoomEvent){
        sendRequest("exit", "");
    }
    @Subscribe
    public void sendStatusClient(StatusEvent statusEvent){
        sendRequest("status", statusEvent.getStatus());
    }
    @Subscribe
    public void sendRows(RowsEvent rowsEvent){
        sendRequest("rows", rowsEvent.getNumberRows());
    }
    public void sendRequest(String prefix, String mess) {
        try {
            transmission.sendObject(new Request(prefix, mess));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
