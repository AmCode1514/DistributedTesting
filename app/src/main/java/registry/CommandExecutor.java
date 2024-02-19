package registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import node.NodeData;
import node.Overlay;
import node.RegisteredNodeData;
import node.requests.LinkWeightsRequest;
import node.requests.MessagingNodesListRequest;
import node.requests.NodeTrafficSummaryRequest;
import node.requests.StartRequest;
import transport.Connection;

public class CommandExecutor {
    NodeData nodeReference;
    Overlay overlay;
    public CommandExecutor(NodeData data) {
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
                    case "list-weights": 
                    break;
                    case "list-messaging-nodes": 
                    break;
                    case "setup-overlay": 
                    int numberOfConnections = Integer.valueOf(command[1]);
                    System.out.println("Attempting to send overlay");
                    RegistryLinkGenerator gen = new RegistryLinkGenerator(overlay);
                    gen.generateLinks(numberOfConnections);
                    sendMessagingNodesList();
                    break;
                    case "send-overlay-link-weights":
                        String nodeAndLinksInformation = linkWeightsRequestStringGenerate();
                        int numberOfLinks = 0;
                        for (Connection conn : nodeReference.getAllConnections()) {
                            LinkWeightsRequest eventToSend = new LinkWeightsRequest(4, nodeReference.getLocalHost(),5000,numberOfLinks,nodeAndLinksInformation);
                            nodeReference.getTCPSend().sendEvent(eventToSend, conn);
                        }
                        break;
                    case "traffic-summary-request":
                        for (Connection conn : nodeReference.getAllConnections()) {
                            NodeTrafficSummaryRequest req = new NodeTrafficSummaryRequest(5,nodeReference.getLocalHost(), 5000, nodeReference);
                            nodeReference.getTCPSend().sendEvent(req, conn);
                        }
                    break;
                    case "start":
                        int rounds = Integer.valueOf(command[1]); 
                        for(Connection conn : nodeReference.getAllConnections()) {
                            StartRequest req = new StartRequest(7, nodeReference.getLocalHost(), 5000, rounds);
                            nodeReference.getTCPSend().sendEvent(req, conn);
                        }
                        break;
                    case "summations":
                    System.out.println("Sent: " + nodeReference.registryGetTotalSentSummation());
                    System.out.println("Received " + nodeReference.registryGetTotalReceivedSummation());

                }

            }
            catch(IOException e) {

            }
        }
    }
    public String linkWeightsRequestStringGenerate() {
        StringBuilder build = new StringBuilder();
        for(int i = 0; i < overlay.size(); ++i) {
            RegisteredNodeData ithNode = overlay.get(i);
            ArrayList<String> ipList = new ArrayList<String>();
            for(String key : ithNode.getNeighborNodesMapKeys()) {
                System.out.println("Key: " + key);
                ipList.add(key);
            }
            for (int j = 0; j < ipList.size(); ++j) {
                build.append(String.format("%s/%s/%s/",ithNode.getIP(),ipList.get(j),ithNode.getLinkWeight(ipList.get(j))));
            }
        }
        String output = build.toString();
        System.out.println(output);
        return output;
    }
    public void sendMessagingNodesList() {
        HashMap<String,ArrayList<String>> unneededConnections = new HashMap<String,ArrayList<String>>();
        //ArrayList<String> messagingListPayloads = new ArrayList<String>();
        for (int i = 0; i < overlay.size(); ++i) {
            String ip = overlay.get(i).getIP();
            unneededConnections.put(ip, new ArrayList<>());
        }
        for (int i = 0; i < unneededConnections.size(); ++i) {
            RegisteredNodeData data = overlay.get(i);
            String payload = "";
            for (String key : data.getNeighborNodesMapKeys()) {
                if (!unneededConnections.get(data.getIP()).contains(key)) {
                    payload += String.format("%s/%s/", key, data.getNeighbor(key).getPort());
                    unneededConnections.get(key).add(data.getIP());
                }
            }
            if (payload.equals("")) {
            }
            else {
            System.out.println(payload);
            MessagingNodesListRequest eventToSend = new MessagingNodesListRequest(3, data.getIP(), data.getPort(), payload);
            nodeReference.getTCPSend().sendEvent(eventToSend,nodeReference.getConnection(data.getIP()));
            }
        }

    }
}
