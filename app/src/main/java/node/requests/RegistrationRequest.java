package node.requests;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import node.Event;
import node.NodeData;
import node.RegisteredNodeData;
import transport.Connection;

public class RegistrationRequest implements Event {
    private final int requestType;
    private final String ipAddress;
    private final int portNumber;
    
    public RegistrationRequest(int requestType, String ipAddress, int portNumber) {
        this.requestType = requestType;
        this. ipAddress = ipAddress;
        this.portNumber = portNumber;
        //this.bytes = bytes;
    }
    @Override
    public int getRequestType() {
        return requestType;
    }
    //do something, potentially create new request and send
    public void OnEvent(NodeData data) {
        System.out.println("Registration request requestType:" + requestType);
        System.out.println("Registration request ip:" + ipAddress);
        System.out.println("Registration request port:" + portNumber);
        Connection conn = data.getConnection(ipAddress);
        int statusCode;
        String additionalInformation;
        if (conn != null) {
            statusCode = 1;
            additionalInformation = "Connection established, all good. There are " + data.getNumberOfConnections() + " other connections";
        }
        else {
            statusCode = 0;
            additionalInformation = "Connection fetch failed, if you see this message there has been big boo boo";
        }
        RegisteredNodeData nodeDataForOverlay = new RegisteredNodeData(conn, ipAddress, portNumber);
        data.getOverlay().addRegisteredNode(nodeDataForOverlay);
        RegistrationResponse response = new RegistrationResponse(2, conn.getIPAddress(), conn.getPortNumber(), statusCode, additionalInformation);
        data.getTCPSend().sendEvent(response, conn);
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
            System.out.println("Issue converting registration request into byte array");
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
}