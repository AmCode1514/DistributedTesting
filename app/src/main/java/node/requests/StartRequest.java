package node.requests;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import node.Event;
import node.NodeData;
import node.Overlay;
import node.RegisteredNodeData;
import transport.Connection;
public class StartRequest extends Thread implements Event {

    private final int requestType;
    private final String ipAddress;
    private final int portNumber;
    private int numberOfRounds;
    NodeData data;
    //constructor for rebuilding
    public StartRequest(int requestType, String ipAddress, int portNumber, byte[] bytes, NodeData data) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.data = data;
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
    public void OnEvent() {
        Overlay overlay = data.getOverlay();
        RegisteredNodeData source = overlay.getByIp(data.getLocalHost());
        Random rand = new Random();
        String[] path;
        for (int i = 0; i < numberOfRounds; ++i) {
            path = overlay.getUsableShortestPathToSource(overlay.get(rand.nextInt(overlay.size())), source);
            while(path.length == 1) {
                path = overlay.getUsableShortestPathToSource(overlay.get(rand.nextInt(overlay.size())), source);
            }
            for (int j = 0; j < 5; ++j) {
                String actualPath = "";
                for (int k = 0; k < path.length - 1; ++k) {
                    actualPath += path[k] + " ";
                }
                actualPath += path[path.length - 1];
                System.out.println("Message Path at Start Request: " + actualPath);
                synchronized(data) {
                    Connection nextNodeConnection = data.getConnection(path[1]);
                    int payload = rand.nextInt(Integer.MAX_VALUE - 1);
                    MessageRequest req = new MessageRequest(8, data.getLocalHost(), nextNodeConnection.getPortNumber(), actualPath, payload);
                    data.getTCPSend().sendEvent(req, nextNodeConnection);
                    data.addPayloadSummationOfMessagesSent(payload);
                    data.incrementMessagesSent();
                }
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
    public void run() {
        OnEvent();
    }
}
