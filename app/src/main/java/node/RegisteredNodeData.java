package node;

import java.util.ArrayList;
import java.util.HashMap;

import transport.Connection;
import transport.TCPReceive;

public class RegisteredNodeData {
    Connection conn;
    String registeredNodeIP; 
    int registeredNodeServerPort;
    //ArrayList<Integer> linkWeights = new ArrayList<Integer>();
    //ArrayList<RegisteredNodeData> neighborNodes = new ArrayList<RegisteredNodeData>();
    HashMap<RegisteredNodeData, Integer> neighborNodesAndWeights = new HashMap<RegisteredNodeData, Integer>();
    boolean connected;
    public RegisteredNodeData(Connection conn, String registeredNodeIP, int registeredNodeServerPort) {
        this.conn = conn;
        this.registeredNodeIP = registeredNodeIP;
        this.registeredNodeServerPort = registeredNodeServerPort;
        connected = true;
    }
    public RegisteredNodeData(String registeredNodeIP, int registeredNodeServerPort) {
        this.registeredNodeIP = registeredNodeIP;
        this.registeredNodeServerPort = registeredNodeServerPort;
        connected = false;
    }
    public boolean hasConnection() {
        return connected;
    }
    public void addNeighbor(RegisteredNodeData neighborNode, int linkWeight) {
        neighborNodesAndWeights.put(neighborNode, linkWeight);
    }
}
