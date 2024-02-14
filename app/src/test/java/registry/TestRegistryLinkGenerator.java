package registry;
import org.junit.Test;
import org.junit.Assert.*;

import node.Overlay;
import node.RegisteredNodeData;
public class TestRegistryLinkGenerator {
    @Test
    public void testLinkWeights() {
        Overlay overlay = new Overlay();
        int nodeNumber = 0;
        for (int i = 0; i < 6; ++i) {
            RegisteredNodeData data = new RegisteredNodeData(String.valueOf(++nodeNumber), 5000);
            overlay.addRegisteredNode(data);
        }
        RegistryLinkGenerator gen = new RegistryLinkGenerator(overlay);
        gen.generateLinks(2);
        for (int i = 0; i < 6; ++i) {
            System.out.println("Current Node: " + overlay.get(i).getIP() + "\n Neighbors");
            for (String key : overlay.get(i).getNeighborNodesMapKeys()) {
                System.out.println(key);
            }
        }
    }
}
