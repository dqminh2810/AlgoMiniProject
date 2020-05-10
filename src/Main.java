import FordFukerson.FlowNetwork;
import MinCut.Graph;
import Utils.RandomConnectedGraph;
import Utils.TestCase;
import Utils.TestCut;

import java.io.FileNotFoundException;

public class Main {
	static final String simpleInput = "resources/SimpleInput.txt";
	static final String mincutInput = "resources/MinCutInput.txt";
	static final String randomGraph = "resources/randomGraph.txt";
	
    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {

		TestCase.testCase(20, 5);
    	/*String inputFileName = randomGraph;
        //Generate random graph with given nb of edges
        RandomConnectedGraph r= new RandomConnectedGraph(50);
        r.buildRandomGraph();
        //r.printAdjacents();
        r.exportTxtFile();

        //Test with min cut algo
        ClassLoader loader = Main.class.getClassLoader();
        Graph g = new Graph(inputFileName);
        //g.printGraph();
        //System.out.println(g.calculateMaxFlowValue(g.vertices.get(3), g.vertices.get(10)));
        //System.out.println(g.calculateMaxFlowValue());
        System.out.println("Karger mincut result: " + g.findMinCut(2, (int) (1+g.getNumberOfVertices()/Math.sqrt(2))));
        
        //Test with ford fukerson algo
        FlowNetwork f = new FlowNetwork(inputFileName);
        //f.printGraph();
        //System.out.println(g.calculateMaxFlowValue(g.vertices.get(3), g.vertices.get(10)));
        //f.setSrcSink(10, 7);
        System.out.println("Ford Fukerson maxflow result: " + f.calculateMaxFlowValue());*/
    	
    }
}
