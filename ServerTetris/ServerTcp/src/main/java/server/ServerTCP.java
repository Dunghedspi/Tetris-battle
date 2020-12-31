package server;

import java.net.*;
import java.util.ArrayList;

class ServerTCP {

    public static String host;
    private static ServerSocket welcomeSocket;
    private static ArrayList<Connection> connections;
    public static ArrayList<Room> rooms;

    public static void main(String[] argv) throws Exception {
        connections = new ArrayList<Connection>();
        rooms = new ArrayList<Room>();
        int count = 0;

        host = InetAddress.getLocalHost().getHostAddress();
        System.out.println(host);
        welcomeSocket = new ServerSocket(6788);
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            connections.add(new Connection(connectionSocket, count++));
            connections.get(connections.size() - 1).start();
        }
    }
}