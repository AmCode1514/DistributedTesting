package transport;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import node.EventHandler;
import node.NodeData;
import node.EventFactory;

public class Server extends Thread {
    private ServerSocket socket;
    private final EventHandler handler;
    private final NodeData nodeData;
    private int portNumber;
    private String ipAddress;
    public Server(ServerSocket socket, NodeData nodeData) {
        this.socket = socket;
        this.nodeData = nodeData;
        portNumber = socket.getLocalPort();
        ipAddress = socket.getInetAddress().getHostName();
        //the event handler produced here will have a reference propagated throughout the TCP receivers, 
        handler = EventHandler.getInstance(this.nodeData);
    }
    public Connection establishConnection(String ipAddress, int port) {
        boolean portFound = false;
        int newPort = port;
        while (!portFound) {
            try{
                if(newPort > port + 100) {
                    System.out.println("Tried to connect using more than 100 ports, terminating attempts.");
                    break;
                }
                Socket newConnection = new Socket(InetAddress.getByName(ipAddress), port);
                if (newConnection.isConnected()) {
                    TCPReceive receiver = new TCPReceive(newConnection, handler);
                    Connection tcpConnection = new Connection(receiver, newConnection);
                    nodeData.addConnection(tcpConnection);
                    tcpConnection.startReceiver();
                    return tcpConnection;
        }
    }
        catch(IOException e) {
            ++newPort;
            System.out.println(String.format("Previous port:%d next port: %d", newPort - 1, newPort));
        }
        }
        return null;
    }
    
    public void startServer() {
        handler.start();
        while (!socket.isClosed()) {
            try {
                Socket connectionSocket = socket.accept();
                // System.out.println(connectionSocket.getInetAddress().getHostName());
                // System.out.flush();
                TCPReceive receiver = new TCPReceive(connectionSocket, handler);
                Connection tcpConnection = new Connection(receiver, connectionSocket);
                synchronized(nodeData) {
                    nodeData.addConnection(tcpConnection);
                }
                tcpConnection.startReceiver();
            }
            catch(IOException e) {
                System.out.println("Error occurred accepting connection through server socket");
                e.printStackTrace();
            }

        }
    }
    public int getServerPort() {
        return portNumber;
    }

    public String getServerIpAddress() {
        return ipAddress;
    }
    
    public void run() {
        startServer();
    }
}
