package registry;
import org.junit.Test;
import org.junit.Assert.*;

import node.Djikstra;
import node.Overlay;
import node.RegisteredNodeData;
public class TestRegistryLinkGenerator {
    @Test
    public void testLinkWeights() {
        Overlay overlay = new Overlay();
        int nodeNumber = 0;
        for (int i = 0; i < 10; ++i) {
            RegisteredNodeData data = new RegisteredNodeData(String.valueOf(++nodeNumber), 5000);
            overlay.addRegisteredNode(data);
        }
        RegistryLinkGenerator gen = new RegistryLinkGenerator(overlay);
        gen.generateLinks(4);
        for (int i = 0; i < 10; ++i) {
            System.out.println("Current Node: " + overlay.get(i).getIP() + "\n Neighbors");
            for (String key : overlay.get(i).getNeighborNodesMapKeys()) {
                System.out.println(key + " Weight " + overlay.get(i).getLinkByIp(key).getLinkWeight());
            }
        }
        Djikstra djik = new Djikstra(overlay, overlay.get(0));
        djik.doDjikstra();

        for (int i = 0; i < overlay.size(); ++i) {
            System.out.println("Node " + overlay.get(i).getIP());
            for (RegisteredNodeData t : overlay.get(i).shortestPath) {
                System.out.println("Node in Path: " + t.getIP());
            }
        }
        // for (int i = 0; i < overlay.size(); ++i) {
        //     System.out.println("Node: " + overlay.get(i).getIP() + " " + overlay.get(i).getDistance());
        // }
    }
    @Test
    public void testDjikstra() {
    }
}
