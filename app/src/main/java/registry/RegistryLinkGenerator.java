package registry;

import java.util.Random;

import node.Link;
import node.Overlay;

public class RegistryLinkGenerator {
    Overlay overlay;
    public RegistryLinkGenerator(Overlay overlay) {
        this.overlay = overlay;
    }
    //ITS ALIVE!!!
    public void generateLinks(int numberOfLinks) {
        Random rand = new Random();
        for (int i = 0; i < overlay.size(); ++i) {
            if (i == overlay.size() - 1) {
                int random = rand.nextInt(10) + 1;
                overlay.get(i).addNeighbor(new Link(overlay.get(0), random));
                overlay.get(0).addNeighbor(new Link(overlay.get(i), random));
            }
            else {
                int random = rand.nextInt(10) + 1;
                overlay.get(i).addNeighbor(new Link(overlay.get(i+1), random));
                overlay.get(i+1).addNeighbor(new Link(overlay.get(i), random));
            }
        }
            for (int i = 0; i < overlay.size(); ++i) {
                for (int k = i; k < overlay.size(); ++k) {
                    if (overlay.get(i).getNumberOfLinks() < numberOfLinks && overlay.get(k).getNumberOfLinks() < numberOfLinks && !(i==k) && !overlay.get(i).isNeighbor(overlay.get(k).getIP())) {
                        int random = rand.nextInt(10) + 1;
                        overlay.get(i).addNeighbor(new Link(overlay.get(k), random));
                        overlay.get(k).addNeighbor(new Link(overlay.get(i), random));
                    }
                    if (overlay.get(i).getNumberOfLinks() == numberOfLinks) {
                        break;
                    }
                }
            }
    }
}
