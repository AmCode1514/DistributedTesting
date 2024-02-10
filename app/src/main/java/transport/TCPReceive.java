package transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import node.EventFactory;
import node.EventHandler;
import node.Event;

public class TCPReceive extends Thread {
    private final Socket connectionSocket;
    private final EventHandler handler;

    public TCPReceive(Socket connectionSocket, EventHandler handler) {
        this.connectionSocket = connectionSocket;
        this.handler = handler;
    }
    public void closeConnection() {
        try{
        connectionSocket.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    private void passToEventHandler(byte[] message) {
         handler.parseAndAddEvent(message);
    }
    public void run() {
        try (
            DataInputStream in = new DataInputStream(connectionSocket.getInputStream());
        ) {
        while(!connectionSocket.isClosed()) {
            int messageSize = in.readInt();
            byte[] message = new byte[messageSize];
            in.readFully(message);
            passToEventHandler(message);
        }
     }
     catch(IOException e) {
        System.out.println("Stream failure or socket closed, shutting down");
        e.printStackTrace();
    }
    }
}
