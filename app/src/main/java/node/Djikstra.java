package node;

import java.util.ArrayList;

public class Djikstra {
    Overlay overlay;
    RegisteredNodeData source;
    public Djikstra(Overlay overlay, RegisteredNodeData source) {
        this.overlay = overlay;
        this.source = source;
    }
    public void doDjikstra() {
        // ArrayList<RegisteredNodeData> q = new ArrayList<RegisteredNodeData>();
        // // RegisteredNodeData[] prev = new RegisteredNodeData[overlay.size()];
        // // Integer[] dist = new Integer[overlay.size()];
        // // q.add(source);
        // for (int i = 0; i < overlay.size(); ++i) {
        //     // if (!overlay.get(i).equals(source)) {
        //     //     q.add(overlay.get(i));
        //     // }
        //     q.add(overlay.get(i));
        // }
        // source.setDistance(0);
        // while (!q.isEmpty()) {
        //     RegisteredNodeData vx = getShortestDistance(q);
        //     for (String key : vx.getNeighborNodesMapKeys()) {
        //         RegisteredNodeData neighbor = vx.getLinkByIp(key).getRegisteredNodeData();
        //         int alt = vx.getDistance() + vx.getLinkWeight(key);
        //         if (alt < neighbor.getDistance()) {
        //             neighbor.setDistance(alt);
        //             neighbor.addShortestNode(vx);
        //         }
        //     }
        // }
        
        int overlaySize = overlay.size();
        int[][] graph = new int[overlaySize][overlaySize];
        for (int i = 0; i < overlaySize; ++i) {
            for (int j = 0; j < overlaySize; ++j) {
                Link dist = overlay.get(i).getLinkByIp(overlay.get(j).getIP());
                //System.out.println(dist.getLinkWeight());
                if (dist != null) {
                    if (i != j) {
                        graph[i][j] = dist.getLinkWeight();
                    }
                }
                else {
                    graph[i][j] = -1;
                }
            }
        }
        for (int b : dijkstra(graph, overlay.getIndexByIp(source.getIP()))) {
            System.out.print(b + " ");
        }
        System.out.println();

        // for (int[] row : graph) {
        //     for (int i : row) {
        //         System.out.print(i + " ");
        //     }
        //     System.out.println("\n");
       // }
    }
    private int minimumDistance(int distance[], Boolean spSet[])  {   
        int m = Integer.MAX_VALUE;
        int m_index = -1;  
  
        for (int vx = 0; vx < overlay.size(); vx++)  {  
            if (spSet[vx] == false && distance[vx] <= m) {  
                m = distance[vx];  
                m_index = vx;  
            }  
        }  
        return m_index;     
    }  

    private int[] dijkstra(int graph[][], int s)  {
        int totalVertex = overlay.size();  
        int distance[] = new int[totalVertex]; 
    
        Boolean spSet[] = new Boolean[totalVertex];  
    
        for (int j = 0; j < totalVertex; j++) {  
        distance[j] = Integer.MAX_VALUE;  
        spSet[j] = false;  
        }  
   
        distance[s] = 0;
        ArrayList<ArrayList<Integer>> shortestPath = new ArrayList<ArrayList<Integer>>(totalVertex);
        int px = minimumDistance(distance, spSet);
        for (int cnt = 0; cnt < totalVertex - 1; ++cnt)   {   
            int ux = minimumDistance(distance, spSet);  
            spSet[ux] = true;
            //System.out.println("Outer loop ux: "  + ux);
            // if (shortestPath.size() > 0) {
            //     shortestPath.add(new ArrayList<Integer>());
            //     for (int i = 0; i < shortestPath.get(shortestPath.size() -2).size(); ++i) {
            //         shortestPath.get(shortestPath.size()-1).add(shortestPath.get(shortestPath.size() -2).get(i));
            //     }
            // }
            // else {
            //     shortestPath.add(new ArrayList<Integer>());
            //     //shortestPath.get(0).add(ux);
            // }
            for (int vx = 0; vx < totalVertex; vx++) {
                if (!spSet[vx] && graph[ux][vx] != -1 && distance[ux] != Integer.MAX_VALUE && distance[ux] + graph[ux][vx] < distance[vx]) {  
                    distance[vx] = distance[ux] + graph[ux][vx];
                    overlay.get(vx).addShortestNode(overlay.get(ux));
                    //System.out.println("Inner loop condition: "  + vx);
                    //shortestPath.get(ux).add(vx);
                }
                //System.out.println("Inner loop: " + vx);
            }
        }
        return distance; 
    } 

    public RegisteredNodeData getShortestDistance(ArrayList<RegisteredNodeData> q) {
        int indexOfShortest = 0;
        for (int i = 0; i < q.size(); ++i) {
            if (q.get(i).getDistance() < q.get(indexOfShortest).getDistance()) {
                indexOfShortest = i;
            }
        }
        RegisteredNodeData shortestDistNode = q.get(indexOfShortest);
        q.remove(indexOfShortest);
        return shortestDistNode;
    }
}
