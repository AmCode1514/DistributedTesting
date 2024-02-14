package node;

import java.util.ArrayList;

import registry.RegistryLinkGenerator;

public class Overlay {
    //HashMap<String, RegisteredNodeData> registeredNodeInformation = new HashMap<String, RegisteredNodeData>();
    ArrayList<RegisteredNodeData> registeredNodeInformation = new ArrayList<RegisteredNodeData>();
    public Overlay() {
    }
    public void addRegisteredNode(RegisteredNodeData data) {
        registeredNodeInformation.add(data);
    }
    public RegisteredNodeData get(int i) {
        return registeredNodeInformation.get(i);
    }
    public int size() {
        return registeredNodeInformation.size();
    }
    public boolean containsIP(String ip) {
        for (int i = 0; i < registeredNodeInformation.size(); ++i) {
            if (registeredNodeInformation.get(i).getIP() == ip) {
                return true;
            }
        }
        return false;
    }
    public RegisteredNodeData getByIp(String ip) {
        for (int i = 0; i < registeredNodeInformation.size(); ++i) {
            if (registeredNodeInformation.get(i).getIP() == ip) {
                return registeredNodeInformation.get(i);
            }
        }
        return null;
    }
    public void printNodesAndLinks() {
        for (int i = 0; i < registeredNodeInformation.size(); ++i) {
            System.out.println("Current Node: " + registeredNodeInformation.get(i).getIP() + "\n Neighbors");
            for (String key : registeredNodeInformation.get(i).getNeighborNodesMapKeys()) {
                System.out.println(key);
            }
        }
    }
    }
