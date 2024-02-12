package node;

import transport.TCPReceive;
import transport.TCPSend;

import java.util.ArrayList;

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
}
