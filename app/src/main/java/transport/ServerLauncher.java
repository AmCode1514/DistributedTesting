package transport;
import java.io.IOException;
import java.net.*;

import node.NodeData;

public class ServerLauncher {
    private int port;
    private NodeData nodeData;
    public ServerLauncher(int port, NodeData nodeData) {
        this.port = port;
        this.nodeData = nodeData;
    }
    public Server getServer() throws IOException {
            ServerSocket socket = new ServerSocket();
            bindPort(socket);
            return new Server(socket, nodeData);
        }
    private void bindPort(ServerSocket socket) {
        while(!socket.isBound()) {
            try{
                socket.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
                System.out.println("ServerSocket binded ipAddress: " + socket.getInetAddress().getHostName());
            
            }
            catch(IOException e) {
                ++port;
            }
        }
    }
}
