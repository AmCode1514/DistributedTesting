package node.requests;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import node.Event;
import node.NodeData;
import transport.Connection;
public class MessageRequest extends Thread implements Event{
    private final int requestType;
    private final String ipAddress;
    private final int portNumber;
    private String routingPath;
    private int payload;
    //constructor for rebuilding
    NodeData data;
    
    public MessageRequest(int requestType, String ipAddress, int portNumber, byte[] bytes, NodeData data) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.data = data;
        unPackData(bytes);
    }
    //constructor for use
    public MessageRequest(int requestType, String ipAddress, int portNumber, String routingPath, int payload) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.payload = payload;
        this.routingPath = routingPath;
    }
    @Override
    public int getRequestType() {
        return requestType;
    }
    @Override
    public void OnEvent() {
        String[] route = routingPath.split(" ");
        ArrayList<String> mid = new ArrayList<String>(Arrays.asList(route));
        mid.remove(0);
        String[] updatedRoute = mid.toArray(new String[mid.size()]);
        // System.out.println("Original route: ");
        // for (String t : route) {
        //     System.out.println(t);
        // }
        // System.out.println("New route: ");
        // for (String t : updatedRoute) {
        //     System.out.println(t);
       // }
        if (updatedRoute.length == 1) {
            synchronized(data) {
                data.incrementMessagesReceived();
                data.addPayloadSummationOfMessagesReceived(payload);
            }
        }
        else if (updatedRoute.length > 1) {
            synchronized(data) {
                System.out.println("I am Node: " + data.getLocalHost() + " and I am attempting to route path " + routingPath);
               Connection conn = data.getConnection(updatedRoute[1]);
                String newRoute = "";
                for (int i = 0; i < updatedRoute.length; ++i) {
                    if (i == updatedRoute.length - 1) {
                       newRoute += updatedRoute[i];
                 }
                else {
                    newRoute += updatedRoute[i] + " ";
                }
            }
                System.out.println("New Message route: " + newRoute);
                data.getTCPSend().sendEvent((new MessageRequest(requestType, data.getLocalHost(), conn.getPortNumber(), newRoute, payload)), conn);
                data.incrementMessagesRelayed();
        }
        }
        else {
           System.out.println("Something is wrong, look at the message nodes relaying"); 
        }
    }
    @Override
    public byte[] reMarshallToBasic() {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
        try {
        HeaderMarshall.generateRequestHeaderByteArray(dout, requestType, ipAddress, portNumber);
        dout.writeInt(payload);
        byte[] routingPathBytes = routingPath.getBytes();
        dout.writeInt(routingPathBytes.length);
        dout.write(routingPathBytes);
        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        dout.close();
        return marshalledBytes;
        }
        catch (IOException e) {
            System.out.println("Issue converting message request  into byte array");
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
            payload = din.readInt();
            int routingPathSize = din.readInt();
            byte[] routingPathBytes = new byte[routingPathSize];
            din.readFully(routingPathBytes);
            routingPath = new String(routingPathBytes);
        }
        catch(IOException e) {
            System.out.println("Issue converting payload to additional fields in Message Request");
            e.printStackTrace();
        }
    }
    public void run() {
        OnEvent();
    }
}


