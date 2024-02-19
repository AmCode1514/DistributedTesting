package messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import node.NodeData;
import node.Overlay;
import node.RegisteredNodeData;
import node.requests.DeregisterRequest;
import node.requests.LinkWeightsRequest;
import node.requests.MessagingNodesListRequest;
import node.requests.NodeTrafficSummaryRequest;
import node.requests.StartRequest;
import transport.Connection;

public class MessengerCommandExecutor {
    NodeData nodeReference;
    Overlay overlay;
    public MessengerCommandExecutor(NodeData data) {
        nodeReference = data;
        overlay = data.getOverlay();
    }
    //As a note, the portnumbers in the message headers do not need to be accurate, the registry and messenger nodes have a hashmap that can be easily used
    //to access correct port numbers based on the name alone. This was a design oversight, and I didn't realize until later that it was unnecessary.
    public void startCommandInput() {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        while(true) {
            try {
                String[] command = reader.readLine().split(" ");
                switch(command[0]) {
                    case "print-shortest-path":
                    RegisteredNodeData source = overlay.getByIp(nodeReference.getLocalHost()); 
                    for (int i = 0; i < overlay.size(); ++i) {
                        String [] path = overlay.getUsableShortestPathToSource(overlay.get(i), source);
                        for (int j = 0; j < path.length - 1; ++j) {
                            System.out.print(path[j] + " " + overlay.getByIp(path[j]).getLinkByIp(path[j + 1]).getLinkWeight() + " ");
                        }
                        System.out.println(path[path.length - 1]);
                    }
                    break;
                    case "exit-overlay":
                    synchronized(nodeReference) {
                    DeregisterRequest req = new DeregisterRequest(10, nodeReference.getLocalHost(), nodeReference.getServer().getServerPort(), nodeReference);
                    Connection conn = nodeReference.getRegistry();
                    nodeReference.getTCPSend().sendEvent(req, conn);
                    }
                    break;


                }

            }
            catch(IOException e) {

            }
        }
    }
   

    }
