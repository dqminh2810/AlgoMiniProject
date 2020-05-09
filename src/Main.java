import FordFukerson.FlowNetwork;
import MinCut.Graph;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, CloneNotSupportedException {


        ClassLoader loader = Main.class.getClassLoader();
        Graph g = new Graph("resources/SimpleInput.txt");
        g.printGraph();
        //System.out.println(g.calculateMaxFlowValue(g.vertices.get(3), g.vertices.get(10)));
        //System.out.println(g.calculateMaxFlowValue());
        System.out.println(g.findMinCut());


        FlowNetwork f = new FlowNetwork("resources/SimpleInput.txt");
        //f.printGraph();
        //System.out.println(g.calculateMaxFlowValue(g.vertices.get(3), g.vertices.get(10)));
        //f.setSrcSink(10, 7);
        System.out.println(f.calculateMaxFlowValue());

    }
}
