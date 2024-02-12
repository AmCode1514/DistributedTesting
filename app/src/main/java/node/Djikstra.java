package node;

public class Djikstra {
    Overlay overlay;
    RegisteredNodeData source;
    public Djikstra(Overlay overlay, RegisteredNodeData source) {
        this.overlay = overlay;
        this.source = source;
    }
}
