package registry;

import transport.Connection;
import transport.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import node.NodeData;
import node.Overlay;
import transport.TCPReceive;
import transport.TCPSend;

public class RegistryData implements NodeData {
    Connection registry;
    Overlay overlay = new Overlay();
    HashMap<String,Connection> connectionList = new HashMap<String,Connection>();
    TCPSend sender = new TCPSend();
    int numberOfMessagesReceived = 0;
    long summationOfMessagesSent;
    int numberOfMessagesSent;
    Server primaryServerInstance;

    public RegistryData() {

    }
    public void terminateConnection(String ipAddress) {
        connectionList.get(ipAddress).terminateReceiver();
        connectionList.remove(ipAddress);
    }
    @Override
    public void addConnection(Connection currConnection) {
        if (currConnection.getIPAddress().contains(".cs.colostate.edu")) {
            String scrubbedDomain = currConnection.getIPAddress().replace(".cs.colostate.edu", "");
            System.out.println("Registry Scrubbed domain string: " + scrubbedDomain);
            connectionList.put(scrubbedDomain, currConnection);
            return;
        }
        System.out.println("Registry Connection add ip:" + currConnection.getIPAddress());
        connectionList.put(currConnection.getIPAddress(), currConnection);
    }
    @Override
    public Connection getConnection(String ipAddress) {
        return connectionList.get(ipAddress);
    }
    @Override
    public TCPSend getTCPSend() {
        return sender;
    }
    @Override
    public Connection getRegistry() {
        throw new UnsupportedOperationException("this is the registry doofus");
    }
    @Override
    public void setRegistry(Connection conn) {
        throw new UnsupportedOperationException("this is the registry doofus");
    }
    public void setPrimaryServer(Server primaryServerInstance) {
        this.primaryServerInstance = primaryServerInstance;
    }
    @Override
    public Server getServer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServer'");
    }
    @Override
    public Overlay getOverlay() {
        return overlay;
    }
    @Override
    public int getNumberOfConnections() {
        return connectionList.size();
    }
    @Override
    public ArrayList<Connection> getAllConnections() {
        ArrayList<Connection> returnedListOfConnections = new ArrayList<Connection>();
        for (String key : connectionList.keySet()) {
            returnedListOfConnections.add(connectionList.get(key));
        }
        return returnedListOfConnections;
    }
    @Override
    public String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public int numberOfMessagesSent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'numberOfMessagesSent'");
    }
    @Override
    public long summationOfSentMessages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'summationOfSentMessages'");
    }
    @Override
    public int numberOfMessagesReceived() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'numberOfMessagesReceived'");
    }
    @Override
    public long summationOfReceivedMessages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'summationOfReceivedMessages'");
    }
    @Override
    public int numberOfMessagesRelayed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'numberOfMessagesRelayed'");
    }
}
