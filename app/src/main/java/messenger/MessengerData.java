package messenger;

import transport.Connection;
import transport.Server;

import java.util.ArrayList;
import java.util.HashMap;

import node.NodeData;
import node.Overlay;
import transport.TCPSend;

public class MessengerData implements NodeData {
    Connection registry;

    Server primaryServerInstance;
    int localServerPort;
    String localIPAddress;
    Overlay overlay = new Overlay();

    HashMap<String,Connection> connectionList = new HashMap<String,Connection>();

    private final TCPSend sender = new TCPSend();

    private int numberOfMessagesReceived = 0;
    private long summationOfMessagesSent;
    private int numberOfMessagesSent;


    public MessengerData() {

    }
    public void terminateConnection(String ipAddress) {
        connectionList.get(ipAddress).terminateReceiver();
        connectionList.remove(ipAddress);
    }
    @Override
    public void addConnection(Connection currConnection) {
        if (currConnection.getIPAddress().contains(".cs.colostate.edu")) {
            String scrubbedDomain = currConnection.getIPAddress().replace(".cs.colostate.edu", "");
            System.out.println("Messenger Scrubbed domain string: " + scrubbedDomain);
            connectionList.put(scrubbedDomain, currConnection);
            return;
        }
        System.out.println("Messenger Connection add ip:" + currConnection.getIPAddress());
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
        //todo
        return registry;
    }
    @Override
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
    @Override
    public Overlay getOverlay() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Overlay not available, this is a messengerData node");
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
    public int getNumberOfConnections() {
        return connectionList.size();
    }
}
