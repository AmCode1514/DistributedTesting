package registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import node.NodeData;
import transport.Server;
import transport.ServerLauncher;

public class RegistryLauncher {
    public static void main(String[] args) {
        NodeData registry = new RegistryData();
        ServerLauncher launcher;
        // BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        // System.out.println("Enter desired starting port");
        try {
        int port = Integer.valueOf(args[0]);
        launcher = new ServerLauncher(port, registry);
        System.out.println("Attempting Server Launch");
        Server serverReference = launcher.getServer();
        System.out.println(serverReference.getServerPort());
        Thread serverInstance = serverReference;
        registry.setPrimaryServer(serverReference);
        serverInstance.start();
        System.out.println("server started successfully");
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
}
