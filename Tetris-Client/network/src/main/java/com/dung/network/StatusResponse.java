package com.dung.network;

public enum StatusResponse {
  
    //login
    OK, FAIL,
    //join room
    ROOM, NOT_FOUND, JOIN_ROOM, UPDATE_ROOM,
    //status room
    PUBLIC, PRIVATE,
    //status client
    START, READY, REFUSE,
    //mess
    MESS,
    //row
    ROWS,
    //list brisk
    LIST,
    //Result
    LOST, WIN,
    //quit
    QUIT
}
