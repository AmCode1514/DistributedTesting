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
import transport.Connection;

public class MessagingNodesListRequest implements Event {

    private final int requestType;
    private final String ipAddress;
    private final int portNumber;
    private String peerMessagingList;
    private int numberOfPeers;

    public MessagingNodesListRequest(int requestType, String ipAddress, int portNumber, String peerMessagingList) {
        this.requestType = requestType;
        this. ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.peerMessagingList = peerMessagingList;
    }
    public MessagingNodesListRequest(int requestType, String ipAddress, int portNumber, byte[] payload) {
        this.requestType = requestType;
        this. ipAddress = ipAddress;
        this.portNumber = portNumber;
        unPackData(payload);
    }
    @Override
    public int getRequestType() {
        return requestType;
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
    public void OnEvent(NodeData data) {
        String[] peerMessagingArray = peerMessagingList.split("/");
        for (String t : peerMessagingArray) {
            System.out.println(t);
        }
        for(int i = 0; i < peerMessagingArray.length; i = i + 2) {
            String ipAddress = peerMessagingArray[i];
            int portNumber = Integer.valueOf(peerMessagingArray[i+1]);
            data.getServer().establishConnection(ipAddress, portNumber);
        }
        System.out.println("Message nodes list request received, current list of successfully established connections:");
        for (Connection conn : data.getAllConnections()) {
            System.out.println("Connection: " + conn.getIPAddress());
        }
        if (data.getNumberOfConnections() - 1 != numberOfPeers) {
            System.out.println("Mismatch detected between number of peer nodes requested and actual \nRequested:" + numberOfPeers + "\n Actual:" + data.getNumberOfConnections());
        }
    }

    @Override
    public byte[] reMarshallToBasic() {
       byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
        try {
        HeaderMarshall.generateRequestHeaderByteArray(dout, requestType, ipAddress, portNumber);
        dout.writeInt(numberOfPeers);
        byte[] peerMessagingListBytes = peerMessagingList.getBytes();
        dout.writeInt(peerMessagingListBytes.length);
        dout.write(peerMessagingListBytes);
        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        dout.close();
        return marshalledBytes;
        }
        catch (IOException e) {
            System.out.println("Issue converting MessagingNodesList response into byte array");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void unPackData(byte[] data) {
        ByteArrayInputStream baInputStream = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
        try {
            numberOfPeers = din.readInt();
            int sizeOfPeerMessagingList = din.readInt();
            byte[] peerMessagingListBytes = new byte[sizeOfPeerMessagingList];
            din.readFully(peerMessagingListBytes);
            peerMessagingList = new String(peerMessagingListBytes);
        }
        catch(IOException e) {
            System.out.println("Issue converting payload to additional fields in MessageNodesListRequest response");
            e.printStackTrace();
        }
    }
    
}
