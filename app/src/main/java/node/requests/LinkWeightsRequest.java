package node.requests;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import node.Djikstra;
import node.Event;
import node.Link;
import node.NodeData;
import node.Overlay;
import node.RegisteredNodeData;

public class LinkWeightsRequest implements Event {
    private final int requestType;
    private final String ipAddress;
    private final int portNumber;
    private int numberOfLinks;
    private String linkInformation;
    
    public LinkWeightsRequest(int requestType, String ipAddress, int portNumber, int numberOfLinks, String linkInformation) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.numberOfLinks = numberOfLinks;
        this.linkInformation = linkInformation;
    }
    public LinkWeightsRequest(int requestType, String ipAddress, int portNumber, byte[] data) {
        this.requestType = requestType;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        unPackData(data);
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
    // @Override
    // public void OnEvent(NodeData data) {
    //     String[] parsedLinks = linkInformation.split("/");
    //     for(int i = 0; i < parsedLinks.length; i = i + 5) {
    //         String hostname1 = parsedLinks[i];
    //         int portNumber1 = Integer.valueOf(parsedLinks[i+1]);
    //         String hostname2 = parsedLinks[i+2];
    //         int portNumber2 = Integer.valueOf(parsedLinks[i+3]);
    //         int weight = Integer.valueOf(parsedLinks[i+4]);
    //         String comparisonString1 = hostname1 + portNumber1;
    //         String comparisonString2 = hostname2 + portNumber2;
    //         RegisteredNodeData host1;
    //         RegisteredNodeData host2;
    //         if (comparisonString1.compareTo(comparisonString2) > 0) {
    //             if (overlay.containsIP(hostname1)) {
    //                 host1 = overlay.getByIp(hostname1);
    //                 if (host1 == null) {
    //                     System.out.println("Issue converting Ip string to registered node data in link weights request 1");
    //                 }
    //             }
    //             else {
    //                 host1 = new RegisteredNodeData(hostname1, portNumber1);
    //                 overlay.addRegisteredNode(host1);
    //             }
    //             if (overlay.containsIP(hostname2)) {
    //                 host2 = overlay.getByIp(hostname2);
    //                 if (host2 == null) {
    //                     System.out.println("Issue converting Ip string to registered node data in link weights request 2");
    //                 }
    //             }
    //             else {
    //                 host2 = new RegisteredNodeData(hostname2, portNumber2);
    //                 overlay.addRegisteredNode(host2);
    //             }
    //             host1.addNeighbor(hostname2, weight);
    //             host2.addNeighbor(hostname1, weight);
    //         }
    //     }
    // }
    public void OnEvent(NodeData data) {
        String[] parsedLinks = linkInformation.split("/");
        Overlay overlay = data.getOverlay();
        for(int i = 0; i < parsedLinks.length; i = i + 3) {
            String hostname1 = parsedLinks[i];
            String hostname2 = parsedLinks[i+1];
            int weight = Integer.valueOf(parsedLinks[i+2]);
            RegisteredNodeData host1;
            RegisteredNodeData host2;
            //System.out.println(hostname1 + " linked " + hostname2 + " and compare to is " + (hostname1.compareTo(hostname2) > 0));
            if (hostname1.compareTo(hostname2) > 0) {
                if (overlay.containsIP(hostname1)) {
                    //System.out.println("Found copy in overlay for hostname1 in link weights request");
                    host1 = overlay.getByIp(hostname1);
                    if (host1 == null) {
                        System.out.println("Issue converting Ip string to registered node data in link weights request 1");
                    }
                }
                else {
                    host1 = new RegisteredNodeData(hostname1);
                    overlay.addRegisteredNode(host1);
                }
                if (overlay.containsIP(hostname2)) {
                    host2 = overlay.getByIp(hostname2);
                    if (host2 == null) {
                        System.out.println("Issue converting Ip string to registered node data in link weights request 2");
                    }
                }
                else {
                    host2 = new RegisteredNodeData(hostname2);
                    overlay.addRegisteredNode(host2);
                }
                host1.addNeighbor(new Link(host2, weight));
                host2.addNeighbor(new Link(host1, weight));
            }
        }
        overlay.printNodesAndLinks();
        Djikstra djik = new Djikstra(overlay, overlay.getByIp(data.getLocalHost()));
        djik.doDjikstra();
        System.out.println(overlay.getShortestPathToSource(overlay.get(0), overlay.getByIp(data.getLocalHost())));
    }
    @Override
    public byte[] reMarshallToBasic() {
        byte[] marshalledBytes = null;
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
        try {
        HeaderMarshall.generateRequestHeaderByteArray(dout, requestType, ipAddress, portNumber);
        dout.writeInt(numberOfLinks);
        byte[] linkInformationBytes = linkInformation.getBytes();
        dout.writeInt(linkInformationBytes.length);
        dout.write(linkInformationBytes);
        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();
        dout.close();
        return marshalledBytes;
        }
        catch (IOException e) {
            System.out.println("Issue converting LinkWeightsRequest into byte array");
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void unPackData(byte[] data) {
         ByteArrayInputStream baInputStream = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
        try {
            numberOfLinks = din.readInt();
            int sizeOfLinkWeights = din.readInt();
            byte[] linkWeightsBytes = new byte[sizeOfLinkWeights];
            din.readFully(linkWeightsBytes);
            linkInformation = new String(linkWeightsBytes);
        }
        catch(IOException e) {
            System.out.println("Issue converting payload to additional fields in LinkWeightsRequest");
            e.printStackTrace();
        }
    }
}
