package com.dung.socket;


import java.io.IOException;
import java.net.Socket;

public class ClientTcp extends Thread {
    Socket clientSocket;
    SocketListener socketListener;

    public void run() {
        try {
            String serverAddress = "127.0.0.1";
            clientSocket = new Socket(serverAddress, 6788);
            socketListener = new SocketListener(clientSocket);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() throws IOException {
        socketListener.close();
        clientSocket.close();
    }
}
