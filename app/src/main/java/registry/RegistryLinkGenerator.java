package registry;

import node.Overlay;

public class RegistryLinkGenerator {
    Overlay overlay;
    public RegistryLinkGenerator(Overlay overlay) {
        this.overlay = overlay;
    }
    //ITS ALIVE!!!
    public void generateLinks(int numberOfLinks) {
        for (int i = 0; i < overlay.size(); ++i) {
            if (i == overlay.size() - 1) {
                int random = ((int) Math.random()) % 10;
                overlay.get(i).addNeighbor(overlay.get(0).getIP(), random);
                overlay.get(0).addNeighbor(overlay.get(i).getIP(), random);
            }
            else {
                int random = ((int) Math.random()) % 10;
                overlay.get(i).addNeighbor(overlay.get(i+1).getIP(), random);
                overlay.get(i+1).addNeighbor(overlay.get(i).getIP(), random);
            }
        }
            for (int i = 0; i < overlay.size(); ++i) {
                for (int k = i; k < overlay.size(); ++k) {
                    if (overlay.get(i).getNumberOfLinks() < numberOfLinks && overlay.get(k).getNumberOfLinks() < numberOfLinks && !(i==k) && !overlay.get(i).isNeighbor(overlay.get(k).getIP())) {
                        int random = ((int) Math.random()) % 10;
                        overlay.get(i).addNeighbor(overlay.get(k).getIP(), random);
                        overlay.get(k).addNeighbor(overlay.get(i).getIP(), random);
                    }
                    if (overlay.get(i).getNumberOfLinks() == numberOfLinks) {
                        break;
                    }
                }
            }
    }
}
