package registry;

import java.util.ArrayList;

public class Overlay {
    ArrayList<RegisteredNodeData> registeredNodeInformation = new ArrayList<RegisteredNodeData>();
    int numberOfLinks;
    
    public Overlay() {

    }
    
    public void addRegisteredNode(RegisteredNodeData data) {
        registeredNodeInformation.add(data);
    }
}
