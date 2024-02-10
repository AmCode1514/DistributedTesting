package transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    private final TCPReceive receiver;
    private final Socket connectionSocket;
    private final String ipAddress;
    private final int portNumber;
    private DataOutputStream out;

    public Connection(TCPReceive receiver, Socket connectionSocket) {
        this.receiver = receiver;
        this.connectionSocket = connectionSocket;
        portNumber = connectionSocket.getPort();
        ipAddress = connectionSocket.getInetAddress().getHostName();
        try{
        out = new DataOutputStream(connectionSocket.getOutputStream());
        }
        catch (IOException e) {
            System.out.println("Issue opening output stream in connection class");
            e.printStackTrace();
        }
    }
    public void startReceiver() {
        Thread receiverThread = receiver;
        receiverThread.start();
    }
    public Socket getConnectionSocket() {
        return connectionSocket;
    }
    public void terminateReceiver() {
        receiver.closeConnection();
    }
    public String getIPAddress() {
        return ipAddress;
    }
    public int getPortNumber() {
        return portNumber;
    }
    public DataOutputStream getOutputStream() {
        return out;
    }
}
