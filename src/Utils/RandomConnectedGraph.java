package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomConnectedGraph {

    private ArrayList<Integer> vertices;
    public HashMap<Integer, ArrayList<Integer>> adjacencyList;
    public int nbVertices, nbEdges;

    public RandomConnectedGraph(int nbEdges)
    {
        this.nbEdges = nbEdges;
        int minV = (int) Math.ceil((1 + Math.sqrt(1 + 8 * nbEdges)) / 2);
        int maxV = nbEdges + 1;

        Random random = new Random();
        nbVertices = Math.abs(random.nextInt(maxV - minV) + minV);

        vertices = new ArrayList<>();
        adjacencyList = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 1; i <= nbVertices; i++)
            adjacencyList.put(i, new ArrayList<>());
    }

    public void setEdge(int to, int from)
    {
        if (to > adjacencyList.size() || from > adjacencyList.size())
            System.out.println("The vertices does not exists");
        adjacencyList.get(to).add(from);
        adjacencyList.get(from).add(to);

        /*
        ArrayList<Integer> sls = adjacencyList.get(to);
        sls.add(from);
        ArrayList<Integer> dls = adjacencyList.get(from);
        dls.add(to);
        */
    }

    public ArrayList<Integer> getEdge(int to)
    {
        if (to > adjacencyList.size())
        {
            System.out.println("The vertices does not exists");
            return null;
        }
        return adjacencyList.get(to);
    }


    public void buildRandomGraph(){
        try
        {
            int count = 1, to, from;
            while (count <= nbEdges)
            {
                Random random = new Random();

                from = Math.abs(random.nextInt(nbVertices + 1 - 1) + 1);
                to = Math.abs(random.nextInt(nbVertices + 1 - 1) + 1);

                if(to != from){
                    if(!adjacencyList.get(to).contains(from) && !adjacencyList.get(from).contains(to)){
                        setEdge(to, from);
                        //reg.setEdge(from, to);
                        if(!vertices.contains(to)) vertices.add(to);
                        if(!vertices.contains(from)) vertices.add(from);
                        count++;
                    }
                }
            }

            adjacencyList.forEach((k,v) -> {
               if(v.size() == 0){
                   Random random = new Random();
                   int to2 = k;
                   while(to2 == k){
                        to2 = Math.abs(random.nextInt(nbVertices + 1 - 1) + 1);
                        if(to2!=k) {
                            setEdge(to2, k);
                            nbEdges++;
                        }
                   }
               }
            });
        }
        catch (Exception E)
        {
            System.out.println("Something went wrong");
        }
    }

    public void printAdjacents(){
        System.out.println("Random graph has "+nbVertices+" vertices and " +nbEdges+ " edges");

        adjacencyList.forEach((k,v) -> {
            System.out.print(k+":  ");
            for(int vertexId: v){
                System.out.print(vertexId +"\t");
            }

            System.out.println();
        });
    }

    public HashMap<Integer, ArrayList<Integer>> getAdjacencyList(){
        return adjacencyList;
    }

    public void exportTxtFile(){
        try {
            FileWriter myWriter = new FileWriter(new File("resources", "randomGraph.txt"));
            adjacencyList.forEach((k,v) -> {
                try {
                    myWriter.write(k+"\t");
                    for(int adjacent: v){
                        myWriter.write(adjacent+"\t");
                    }
                    myWriter.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
