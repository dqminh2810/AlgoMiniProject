import FordFukerson.FlowNetwork;
import MinCut.Graph;
import Utils.RandomConnectedGraph;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {

        //Generate random graph with given nb of edges
        RandomConnectedGraph r= new RandomConnectedGraph(25);
        r.buildRandomGraph();
        r.printAdjacents();
        r.exportTxtFile();

        //Test with min cut algo
        ClassLoader loader = Main.class.getClassLoader();
        Graph g = new Graph("resources/randomGraph.txt");
        g.printGraph();
        //System.out.println(g.calculateMaxFlowValue(g.vertices.get(3), g.vertices.get(10)));
        //System.out.println(g.calculateMaxFlowValue());
        System.out.println(g.findMinCut());


        //Test with ford fukerson algo
        FlowNetwork f = new FlowNetwork("resources/randomGraph.txt");
        //f.printGraph();
        //System.out.println(g.calculateMaxFlowValue(g.vertices.get(3), g.vertices.get(10)));
        //f.setSrcSink(10, 7);
        System.out.println(f.calculateMaxFlowValue());





    }
}
