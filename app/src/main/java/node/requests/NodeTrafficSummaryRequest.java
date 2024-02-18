package node.requests;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import node.Event;
import node.NodeData;
import node.RegisteredNodeData;
import transport.Connection;

public class NodeTrafficSummaryRequest extends Thread implements Event {
    private final int requestType;
    private final String ipAddress;
    private final int portNumber;
    NodeData data;
    
    public NodeTrafficSummaryRequest(int requestType, String ipAddress, int portNumber, NodeData data) {
        this.requestType = requestType;
        this. ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.data = data;
        //this.bytes = bytes;
    }
    @Override
    public int getRequestType() {
        return requestType;
    }
    //do something, potentially create new request and send
    public void OnEvent() {
        synchronized(data) {
        Connection conn = data.getConnection(ipAddress);
        TrafficSummaryResponse response = new TrafficSummaryResponse(6,data.getLocalHost(), data.getServer().getServerPort(), data.numberOfMessagesSent(), data.summationOfSentMessages(), data.numberOfMessagesReceived(), data.summationOfReceivedMessages(), data.numberOfMessagesRelayed());
        data.getTCPSend().sendEvent(response, conn);
        }
    }
    @Override
    public byte[] reMarshallToBasic() {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
        try {
        HeaderMarshall.generateRequestHeaderByteArray(dout, requestType, ipAddress, portNumber);
        dout.writeInt(0); //registration request contains no other data
        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        return marshalledBytes;
        }
        catch (IOException e) {
            System.out.println("Issue converting Traffic Summary request into byte array");
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String getIPAddress() {
        return ipAddress;
    }
    @Override
    public int getPort() {
        return portNumber;
    }
    @Override
    public void unPackData(byte[] data) {
        // this method is unnecessary as there is no additional data to unpack in this request at this time. 
        throw new UnsupportedOperationException("Unimplemented method 'unPackData'");
    }
    public void run() {
        OnEvent();
    }
}
