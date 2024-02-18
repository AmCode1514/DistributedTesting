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
            //System.out.println("Compared IP " + ip + " Found ip " + registeredNodeInformation.get(i).getIP());
            if (registeredNodeInformation.get(i).getIP().equals(ip)) {
                return true;
            }
        }
        return false;
    }
    public RegisteredNodeData getByIp(String ip) {
        for (int i = 0; i < registeredNodeInformation.size(); ++i) {
            if (registeredNodeInformation.get(i).getIP().equals(ip)) {
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
    public int getIndexByIp(String ip) {
        for (int i = 0; i < registeredNodeInformation.size(); ++i) {
            if (registeredNodeInformation.get(i).getIP().equals(ip)) {
                return i;
            }
        }
        return -1;
    }
    public String getShortestPathToSource(RegisteredNodeData start, RegisteredNodeData source) {
        if (start.getIP().equals(source.getIP())) {
            return source.getIP();
        }
        else {
            String path = start.getIP() + " " + getShortestPathToSource(start.getDjikstraParentNode(), source);
            return path;
        }
    }
}
