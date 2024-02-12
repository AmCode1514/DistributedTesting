package node;

import java.util.ArrayList;

import transport.Connection;
import transport.TCPReceive;

public class RegisteredNodeData {
    Connection conn;
    String registeredNodeIP; 
    int registeredNodeServerPort;
    int linkWeight;
    ArrayList<RegisteredNodeData> neighborNodes = new ArrayList<RegisteredNodeData>();
    public RegisteredNodeData(Connection conn, String registeredNodeIP, int registeredNodeServerPort) {
        this.conn = conn;
        this.registeredNodeIP = registeredNodeIP;
        this.registeredNodeServerPort = registeredNodeServerPort;
    }
}
