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
import transport.Connection;

public class CommandExecutor {
    NodeData nodeReference;
    Overlay overlay;
    public CommandExecutor(NodeData data) {
        nodeReference = data;
        overlay = data.getOverlay();
    }
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
                    case "setup-overlay number-of-connections": 
                    linkWeightsRequestStringGenerate();
                    case "send-overlay-link-weights":
                        int numberOfLinks = Integer.valueOf(command[1]);
                        System.out.println("Attempting to send overlay");
                        RegistryLinkGenerator gen = new RegistryLinkGenerator(overlay);
                        gen.generateLinks(numberOfLinks);
                        String nodeAndLinksInformation = linkWeightsRequestStringGenerate();
                        for (Connection conn : nodeReference.getAllConnections()) {
                            LinkWeightsRequest eventToSend = new LinkWeightsRequest(4, conn.getIPAddress(),conn.getPortNumber(),numberOfLinks,nodeAndLinksInformation);
                            nodeReference.getTCPSend().sendEvent(eventToSend, conn);
                        }
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
    public void generateMessagingNodesList() {
        HashMap<String,ArrayList<String>> unneededConnections = new HashMap<String,ArrayList<String>>();
        ArrayList<String> messagingListPayloads = new ArrayList<String>();
        for (int i = 0; i < overlay.size(); ++i) {
            String ip = overlay.get(i).getIP();
            unneededConnections.put(ip, new ArrayList<>());
        }
        for (int i = 0; i < unneededConnections.size(); ++i) {
            RegisteredNodeData data = overlay.get(i);
            String payload = "";
            for (String key : data.getNeighborNodesMapKeys()) {
                if (!unneededConnections.get(data.getIP()).contains(key)) {
                    payload += String.format("%s/%s/", key, overlay.getByIp(key).getPort());
                    unneededConnections.get(key).add(data.getIP());
                }
            }
            if (!payload.equals("")) {
                messagingListPayloads.add(payload);
            }
        }

    }
}
