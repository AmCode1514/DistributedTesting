package messenger;

import transport.Connection;
import transport.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import node.NodeData;
import node.Overlay;
import registry.NodeTrafficData;
import transport.TCPSend;

public class MessengerData implements NodeData {
    Connection registry;

    Server primaryServerInstance;
    int localServerPort;
    String localIPAddress;
    Overlay overlay = new Overlay();
    String localHost;

    HashMap<String,Connection> connectionList = new HashMap<String,Connection>();

    private final TCPSend sender = new TCPSend();

    private volatile int numberOfMessagesSent = 0;
    private volatile long summationOfMessagesSent = 0;
    private volatile int numberOfMessagesReceived = 0;
    private volatile long summationOfReceivedMessages = 0;
    private volatile int numberOfMessagesRelayed = 0;


    public MessengerData() {

    }
    public synchronized void terminateConnection(String ipAddress) {
        connectionList.get(ipAddress).terminateReceiver();
        connectionList.remove(ipAddress);
    }
    //@Override
    public synchronized void addConnection(Connection currConnection) {
        if (currConnection.getIPAddress().contains(".cs.colostate.edu")) {
            String scrubbedDomain = currConnection.getIPAddress().replace(".cs.colostate.edu", "");
            System.out.println("Messenger Scrubbed domain string: " + scrubbedDomain);
            connectionList.put(scrubbedDomain, currConnection);
            return;
        }
        System.out.println("Messenger Connection add ip:" + currConnection.getIPAddress());
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
        //todo
        return registry;
    }
    //@Override
    public void setRegistry(Connection registry) {
        this.registry = registry;
    }
    //this method must be called in the launcher and nowhere else. Should be done before any thread starts.
    public void setPrimaryServer(Server primaryServerInstance) {
        this.primaryServerInstance = primaryServerInstance;

    }
    public Server getServer() {
        return primaryServerInstance;
    }
    //@Override
    public Overlay getOverlay() {
        return overlay;
    }
    //@Override
    public synchronized ArrayList<Connection> getAllConnections() {
        ArrayList<Connection> returnedListOfConnections = new ArrayList<Connection>();
        for (String key : connectionList.keySet()) {
            returnedListOfConnections.add(connectionList.get(key));
        }
        return returnedListOfConnections;
    }
    //@Override
    public synchronized int getNumberOfConnections() {
        return connectionList.size();
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
    public synchronized int numberOfMessagesSent() {
        return numberOfMessagesSent;
    }
    //@Override
    public synchronized long summationOfSentMessages() {
        return summationOfMessagesSent;
    }
    //@Override
    public synchronized int numberOfMessagesReceived() {
        return numberOfMessagesReceived;
    }
    //@Override
    public synchronized long summationOfReceivedMessages() {
        return summationOfReceivedMessages;
    }
    //@Override
    public synchronized int numberOfMessagesRelayed() {
        return numberOfMessagesRelayed;
    }
    //@Override
    public synchronized void incrementMessagesSent() {
        ++numberOfMessagesSent;
    }
    //@Override
    public synchronized void addPayloadSummationOfMessagesSent(int payload) {
       summationOfMessagesSent += payload;
    }
    //@Override
    public synchronized void incrementMessagesReceived() {
        ++numberOfMessagesReceived;
    }
    //@Override
    public synchronized void addPayloadSummationOfMessagesReceived(int payload) {
        summationOfReceivedMessages += payload;
    }
    //@Override
    public synchronized void incrementMessagesRelayed() {
        ++numberOfMessagesRelayed;
    }
    //@Override
    public long registryGetTotalSentSummation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registryGetTotalSentSummation'");
    }
    //@Override
    public void registryAddTotalSentSummation(long pay) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registryAddTotalSentSummation'");
    }
    //@Override
    public long registryGetTotalReceivedSummation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registryGetTotalReceivedSummation'");
    }
    //@Override
    public void registryAddTotalReceivedSummation(long pay) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registryAddTotalReceivedSummation'");
    }
    @Override
    public void addTrafficData(NodeTrafficData data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTrafficData'");
    }
    @Override
    public boolean checkRoundsFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkRoundsFinished'");
    }
    @Override
    public void clearTrafficStats() {
        numberOfMessagesSent = 0;
        summationOfMessagesSent = 0;
        numberOfMessagesReceived = 0;
        summationOfReceivedMessages = 0;
        numberOfMessagesRelayed = 0;
    }
}
