package registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import node.NodeData;

public class CommandExecutor extends Thread {
    NodeData nodeReference;
    public CommandExecutor(NodeData data) {
        nodeReference = data;
    }
    public void startCommandInput() {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        while(true) {
            try {
                String[] command = reader.readLine().split(" ");
                switch(command[0]) {
                    case "list-weights": break;
                    case "list-messaging-nodes": break;
                    case "setup-overlay number-of-connections": break;
                    case "send-overlay-link-weights": break;
                }

            }
            catch(IOException e) {

            }
        }
    }
}
