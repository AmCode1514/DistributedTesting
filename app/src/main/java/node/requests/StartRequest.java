package node.requests;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import node.Event;
import node.NodeData;
import node.Overlay;
import node.RegisteredNodeData;
public class StartRequest implements Event {

    private final int requestType;
    private final String ipAddress;
    private final int portNumber;
    private int numberOfRounds;
    //constructor for rebuilding
    public StartRequest(int requestType, String ipAddress, int portNumber, byte[] bytes) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        unPackData(bytes);
    }
    //constructor for use
    public StartRequest(int requestType, String ipAddress, int portNumber, int numberOfRounds) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.numberOfRounds = numberOfRounds;
    }
    @Override
    public int getRequestType() {
        return requestType;
    }
    public void OnEvent(NodeData data) {
        Overlay overlay = data.getOverlay();
        RegisteredNodeData source = overlay.getByIp(data.getLocalHost());
        for (int i = 0; i < numberOfRounds; ++i) {
            for (int j = 0; j < 5; ++j) {
                
            }
        }
    }
    @Override
    public byte[] reMarshallToBasic() {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
        try {
        HeaderMarshall.generateRequestHeaderByteArray(dout, requestType, ipAddress, portNumber);
        dout.writeInt(numberOfRounds);
        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        dout.close();
        return marshalledBytes;
        }
        catch (IOException e) {
            System.out.println("Issue converting Traffic response into byte array");
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
    public void unPackData(byte data[]) {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
        try {
        numberOfRounds = din.readInt();
        din.close();
        }
        catch(IOException e) {
            System.out.println("Issue converting payload to additional fields in  response");
            e.printStackTrace();
        }
    }
}
