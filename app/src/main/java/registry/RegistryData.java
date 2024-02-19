package registry;

import transport.Connection;
import transport.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import node.NodeData;
import node.Overlay;
import node.requests.NodeTrafficSummaryRequest;
import node.requests.TrafficSummaryResponse;
import transport.TCPReceive;
import transport.TCPSend;

public class RegistryData implements NodeData {
    Connection registry;
    Overlay overlay = new Overlay();
    HashMap<String,Connection> connectionList = new HashMap<String,Connection>();
    ArrayList<NodeTrafficData> dataReports = new ArrayList<NodeTrafficData>();
    TCPSend sender = new TCPSend();
    Server primaryServerInstance;

    long summationReceived = 0;
    long summationSent = 0;
    int nodesFinished = 0;
    public RegistryData() {

    }
    public synchronized void terminateConnection(String ipAddress) {
        connectionList.get(ipAddress).terminateReceiver();
        connectionList.remove(ipAddress);
    }
    //@Override
    public synchronized void addConnection(Connection currConnection) {
        if (currConnection.getIPAddress().contains(".cs.colostate.edu")) {
            String scrubbedDomain = currConnection.getIPAddress().replace(".cs.colostate.edu", "");
            System.out.println("Registry Scrubbed domain string: " + scrubbedDomain);
            connectionList.put(scrubbedDomain, currConnection);
            return;
        }
        System.out.println("Registry Connection add ip:" + currConnection.getIPAddress());
        connectionList.put(currConnection.getIPAddress(), currConnection);
    }
    //@Override
    public synchronized Connection getConnection(String ipAddress) {
        return connectionList.get(ipAddress);
    }
    //@Override
    public synchronized TCPSend getTCPSend() {
        return sender;
    }
    //@Override
    public Connection getRegistry() {
        throw new UnsupportedOperationException("this is the registry doofus");
    }
    //@Override
    public void setRegistry(Connection conn) {
        throw new UnsupportedOperationException("this is the registry doofus");
    }
    public void setPrimaryServer(Server primaryServerInstance) {
        this.primaryServerInstance = primaryServerInstance;
    }
    //@Override
    public Server getServer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServer'");
    }
    //@Override
    public Overlay getOverlay() {
        return overlay;
    }
    //@Override
    public int getNumberOfConnections() {
        return connectionList.size();
    }
    //@Override
    public ArrayList<Connection> getAllConnections() {
        ArrayList<Connection> returnedListOfConnections = new ArrayList<Connection>();
        for (String key : connectionList.keySet()) {
            returnedListOfConnections.add(connectionList.get(key));
        }
        return returnedListOfConnections;
    }
    //@Override
    public String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
    //@Override
    public int numberOfMessagesSent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'numberOfMessagesSent'");
    }
    //@Override
    public long summationOfSentMessages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'summationOfSentMessages'");
    }
    //@Override
    public int numberOfMessagesReceived() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'numberOfMessagesReceived'");
    }
    //@Override
    public long summationOfReceivedMessages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'summationOfReceivedMessages'");
    }
    //@Override
    public int numberOfMessagesRelayed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'numberOfMessagesRelayed'");
    }
    //@Override
    public void incrementMessagesSent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'incrementMessagesSent'");
    }
    //@Override
    public void addPayloadSummationOfMessagesSent(int payload) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPayloadSummationOfMessagesSent'");
    }
    //@Override
    public void incrementMessagesReceived() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'incrementMessagesReceived'");
    }
    //@Override
    public void addPayloadSummationOfMessagesReceived(int payload) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPayloadSummationOfMessagesReceived'");
    }
    //@Override
    public void incrementMessagesRelayed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'incrementMessagesRelayed'");
    }
    //@Override
    public long registryGetTotalSentSummation() {
        return summationSent;
    }
    //@Override
    public synchronized void registryAddTotalSentSummation(long payload) {
        summationSent += payload;
    }
    //@Override
    public synchronized long registryGetTotalReceivedSummation() {
        return summationReceived;
    }
    //@Override
    public void registryAddTotalReceivedSummation(long payload) {
        summationReceived += payload;
    }
    @Override
    public synchronized void addTrafficData(NodeTrafficData data) {
        dataReports.add(data);
        if (dataReports.size() == overlay.size()) {
            int numberOfMessagesSent = 0;
            int numberOfMessagesReceived = 0;
            for(NodeTrafficData trafficData : dataReports) {
                numberOfMessagesSent += trafficData.numberOfMessagesSent;
                numberOfMessagesReceived += trafficData.numberOfMessagesReceived;
                System.out.println(String.format("Node: %s Sent Messages: %s Received Messages: %s Sum of Sent Messages: %s Sum of Received Messages: %s Relayed Messages: %s", trafficData.ipAddress, trafficData.numberOfMessagesSent, trafficData.numberOfMessagesReceived, trafficData.summationOfMessagesSent, trafficData.summationOfReceivedMessages, trafficData.numberOfMessagesRelayed));
            }
            System.out.println(String.format("SUMS: Messages sent: %s Messages Received: %s Sums Of Sent: %s Sums of received: %s", numberOfMessagesSent, numberOfMessagesReceived, registryGetTotalSentSummation(), registryGetTotalReceivedSummation()));
            summationReceived = 0;
            summationSent = 0;
            dataReports.clear();
        }
    }
    @Override
    public synchronized boolean checkRoundsFinished() {
        ++nodesFinished;
        if (nodesFinished == overlay.size()) {
            nodesFinished = 0;
            return true;
        }
        return false;
    }
    @Override
    public void clearTrafficStats() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearTrafficStats'");
    }
    
   
    
    
}
