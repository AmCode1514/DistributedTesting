package node;

public class Link {
    private RegisteredNodeData data;
    private int linkWeight;
    public Link(RegisteredNodeData data, int linkWeight){
        this.data = data;
        this.linkWeight = linkWeight;
    }
    public RegisteredNodeData getRegisteredNodeData() {
        return data;
    }
    public int getLinkWeight() {
        return linkWeight;
    }
}
