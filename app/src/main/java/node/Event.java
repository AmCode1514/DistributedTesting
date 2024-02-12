package node;

public interface Event {
    public int getRequestType();
    public String getIPAddress();
    public int getPort();
    public void OnEvent(NodeData data);
    public byte[] reMarshallToBasic();
    public void unPackData(byte[] data);
} 
