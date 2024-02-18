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
public class TrafficSummaryResponse implements Event {

    private final int requestType;
    private final String ipAddress;
    private final int portNumber;

    private int numberOfMessagesSent;
    private long summationOfMessagesSent;
    private int numberOfMessagesReceived;
    private long summationOfReceivedMessages;
    private int numberOfMessagesRelayed;
    //constructor for rebuilding
    public TrafficSummaryResponse(int requestType, String ipAddress, int portNumber, byte[] bytes) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        unPackData(bytes);
    }
    //constructor for use
    public TrafficSummaryResponse(int requestType, String ipAddress, int portNumber, int numberOfMessagesSent, long summationOfMessagesSent, int numberOfMessagesReceived, long summationOfReceivedMessages, int numberOfMessagesRelayed) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.numberOfMessagesSent = numberOfMessagesSent;
        this.summationOfMessagesSent = summationOfMessagesSent;
        this.numberOfMessagesReceived = numberOfMessagesReceived;
        this.summationOfReceivedMessages = summationOfReceivedMessages;
        this.numberOfMessagesRelayed = numberOfMessagesRelayed;
    }
    @Override
    public int getRequestType() {
        return requestType;
    }
    public void OnEvent(NodeData data) {
        System.out.println(String.format("Node IP: %s \n Node Port: %s \n Number of Messages Sent %s \n Summation Of Sent Messages: %s \n Number of Messages Received: %s \n Summation Of Received Messages: %s \n Number Of Relayed Messages: %s \n\n\n\n",ipAddress, portNumber, numberOfMessagesSent, summationOfMessagesSent, numberOfMessagesReceived, summationOfReceivedMessages, numberOfMessagesRelayed));
    }
    @Override
    public byte[] reMarshallToBasic() {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
        try {
        HeaderMarshall.generateRequestHeaderByteArray(dout, requestType, ipAddress, portNumber);
        dout.writeInt(numberOfMessagesSent);
        dout.writeLong(summationOfMessagesSent);
        dout.writeInt(numberOfMessagesReceived);
        dout.writeLong(summationOfReceivedMessages);
        dout.writeInt(numberOfMessagesRelayed);
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
        this.numberOfMessagesSent = din.readInt();
        this.summationOfMessagesSent = din.readLong();
        this.numberOfMessagesReceived =  din.readInt();
        this.summationOfReceivedMessages = din.readLong();
        this.numberOfMessagesRelayed = din.readInt();
        din.close();
        }
        catch(IOException e) {
            System.out.println("Issue converting payload to additional fields in  response");
            e.printStackTrace();
        }
    }
}
