package com.dung.socket;


import java.net.Socket;

public class ClientTcp {
    Socket clientSocket;
    SocketListener socketListener;

    public ClientTcp() throws Exception {
        String serverAddress = "127.0.0.1";
        clientSocket = new Socket(serverAddress, 6788);
        socketListener = new SocketListener(clientSocket);
    }

    public SocketListener getSocketListener() {
        return socketListener;
    }
}
