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
public class DeregisterResponse extends Thread implements Event {

    private final int requestType;
    private final String ipAddress;
    private final int portNumber;
    private int statusCode;
    private String additionalInformation;
    //constructor for rebuilding
    NodeData data;
    
    public DeregisterResponse(int requestType, String ipAddress, int portNumber, byte[] bytes, NodeData data) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.data = data;
        unPackData(bytes);
    }
    //constructor for use
    public DeregisterResponse(int requestType, String ipAddress, int portNumber, int statusCode, String additionalInformation) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.statusCode = statusCode;
        this.additionalInformation = additionalInformation;
    }
    @Override
    public int getRequestType() {
        return requestType;
    }
    @Override
    public void OnEvent() {
        System.out.println(String.format("Deregistration response received with status code %s and additionInformation: %s", statusCode, additionalInformation));
    }
    @Override
    public byte[] reMarshallToBasic() {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
        try {
        HeaderMarshall.generateRequestHeaderByteArray(dout, requestType, ipAddress, portNumber);
        dout.writeInt(statusCode);
        byte[] additionalInformationBytes = additionalInformation.getBytes();
        dout.writeInt(additionalInformationBytes.length);
        dout.write(additionalInformationBytes);
        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        dout.close();
        return marshalledBytes;
        }
        catch (IOException e) {
            System.out.println("Issue converting registration response  into byte array");
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
            statusCode = din.readInt();
            int additionalInformationSize = din.readInt();
            byte[] additionalInformationBytes = new byte[additionalInformationSize];
            din.readFully(additionalInformationBytes);
            additionalInformation = new String(additionalInformationBytes);
        }
        catch(IOException e) {
            System.out.println("Issue converting payload to additional fields in registration response");
            e.printStackTrace();
        }
    }
    public void run() {
        OnEvent();
    }
}
