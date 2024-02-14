package node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import transport.Connection;
import transport.TCPReceive;
public class RegisteredNodeData {
    Connection conn;
    String registeredNodeIP; 
    int registeredNodeServerPort;
    HashMap<String, Integer> neighborNodesAndWeights = new HashMap<String, Integer>();
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
    public RegisteredNodeData(String registeredNodeIP) {
        this.registeredNodeIP = registeredNodeIP;
    }
    public boolean hasConnection() {
        return connected;
    }
    public void addNeighbor(String neighborNode, int linkWeight) {
        neighborNodesAndWeights.put(neighborNode, linkWeight);
    }
    public int getNumberOfLinks() {
        return neighborNodesAndWeights.size();
    }
    public int getLinkWeight(String ip) {
        return neighborNodesAndWeights.get(ip);
    } 
    public String getIP() {
        return registeredNodeIP;
    }
    public int getPort() {
        return registeredNodeServerPort;
    }
    public boolean isNeighbor(String neighbor) {
        if (neighborNodesAndWeights.containsKey(neighbor)) {
            return true;
        }
        else {
            return false;
        }
    }
    public Set<String> getNeighborNodesMapKeys() {
        return neighborNodesAndWeights.keySet();
    }
}
