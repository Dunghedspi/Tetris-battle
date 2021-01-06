package com.dung.socket;

import com.dung.lib.EventBusCustom;
import com.dung.eventRequest.*;
import com.dung.network.CodeRequest;
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
        sendRequest(CodeRequest.LOST,"");
    }
    @Subscribe
    public void login(LoginEvent loginEvent){
        sendRequest(CodeRequest.USER, loginEvent.getUsername());
    }
    @Subscribe
    public void joinRoomType(RoomTypeEvent roomTypeEvent){
        sendRequest(CodeRequest.TYPE, roomTypeEvent.getTypeRoom());
    }
    @Subscribe
    public void sendMess(MessageEvent messageEvent){
        sendRequest(CodeRequest.MESS,messageEvent.getMessage());
    }
    @Subscribe
    public void exitRoom(ExitRoomEvent exitRoomEvent){
        sendRequest(CodeRequest.EXIT, "");
    }
    @Subscribe
    public void sendStatusClient(StatusEvent statusEvent){
        sendRequest(CodeRequest.STATUS, statusEvent.getStatus());
    }
    @Subscribe
    public void sendRows(RowsEvent rowsEvent){
        sendRequest(CodeRequest.ROWS, rowsEvent.getNumberRows());
    }
    @Subscribe
    public void sendStatusRoom(StatusRoomEvent statusRoomEvent) {sendRequest(CodeRequest.STATUS_ROOM, statusRoomEvent.getStatus());}
    public void sendRequest(CodeRequest codeRequest, String mess) {
        try {
            transmission.sendObject(new Request(codeRequest, mess));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
