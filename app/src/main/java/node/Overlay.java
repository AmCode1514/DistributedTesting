package node;

import java.util.ArrayList;

public class Overlay {
    ArrayList<RegisteredNodeData> registeredNodeInformation = new ArrayList<RegisteredNodeData>();
    int numberOfLinks = 0;
    public Overlay() {
    }
    public void addRegisteredNode(RegisteredNodeData data) {
        registeredNodeInformation.add(data);
        ++numberOfLinks;
    }
    public int getNumberOfLinks() {
        return numberOfLinks;
    }
}
