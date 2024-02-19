package node;

import transport.TCPReceive;
import transport.TCPSend;

import java.util.ArrayList;

import node.requests.TrafficSummaryResponse;
import registry.NodeTrafficData;
import transport.Connection;
import transport.Server;

public interface NodeData {
    public void addConnection(Connection currConnection);
    public Connection getConnection(String ipAddress);
    public void terminateConnection(String ipAddress);
    public TCPSend getTCPSend();
    public Connection getRegistry();
    public void setRegistry(Connection registry);
    public void setPrimaryServer(Server serverInstance);
    public Server getServer();
    public Overlay getOverlay();
    public int getNumberOfConnections();
    public ArrayList<Connection> getAllConnections();
    public String getLocalHost();
    public int numberOfMessagesSent();
    public long summationOfSentMessages();
    public int numberOfMessagesReceived();
    public long summationOfReceivedMessages();
    public int numberOfMessagesRelayed();
    public void incrementMessagesSent();
    public void addPayloadSummationOfMessagesSent(int payload);
    public void incrementMessagesReceived();
    public void addPayloadSummationOfMessagesReceived(int payload);
    public void incrementMessagesRelayed();
    public long registryGetTotalSentSummation();
    public void registryAddTotalSentSummation(long pay);
    public long registryGetTotalReceivedSummation();
    public void registryAddTotalReceivedSummation(long pay);
    public void addTrafficData(NodeTrafficData data);
    public boolean checkRoundsFinished();
    public void clearTrafficStats();
}
