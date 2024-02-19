package messenger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import node.NodeData;
import node.requests.RegistrationRequest;
import transport.Server;
import transport.ServerLauncher;
import messenger.MessengerData;

public class MessengerLauncher {
    public static void main(String[] args) {
        NodeData messenger = new MessengerData();
        ServerLauncher launcher;
        // BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        // System.out.println("Enter desired starting port");
        try {
        int port = Integer.valueOf(args[0]);
        launcher = new ServerLauncher(port, messenger);
        System.out.println("Attempting Server Launch");
        Server serverReference = launcher.getServer();
        Thread serverInstance = serverReference;
        messenger.setPrimaryServer(serverReference);
        System.out.println("Attempting Registry connection");
        messenger.setRegistry(serverReference.establishConnection(args[1], port));
        System.out.println("Registry Connection Successful");
        serverInstance.start();
        RegistrationRequest init = new RegistrationRequest(1, serverReference.getServerIpAddress(), serverReference.getServerPort(), messenger);
        System.out.println("Messenger launcher server ip:" + serverReference.getServerIpAddress());
        messenger.getTCPSend().sendEvent(init, messenger.getRegistry());
        MessengerCommandExecutor exec = new MessengerCommandExecutor(messenger);
        exec.startCommandInput();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
}
