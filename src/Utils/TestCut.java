package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Utils.RandomConnectedGraph;
import FordFukerson.FlowNetwork;
import MinCut.Graph;

public class TestCut {
	
	FileWriter myWriter;
	
	/*
	 * CONSTRUCTORS
	 */
	public TestCut() {
		try {
			this.myWriter = new FileWriter(new File("", "results.txt"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * PRIVATE METHODS
	 */
	private void exportResult(String stats){
		try {
            myWriter.write(stats);
            myWriter.write("\n");
        } 
    	catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private String formatOutput(int GraphSize, int nbOfTries, int nRepeats) {
		String finalOutput = "";
		return finalOutput;
	}
	
	private void test(RandomConnectedGraph r, int nRepeats) {
		if (r.nbEdges <= 70) {
			// Check results with Ford Fukerson algorithm
			// Exepected succes = 1/log(r.nbVertices)
		}
		else {
			// Repeat nRepeats times Karger algorithm
			// Expected succes = 1 - 1/n
		}
	}
	
	/*
	 * PUBLIC METHODS
	 */
	
	public void doTest() {
		try	{
			System.out.println("---- Starting tests ----");
			RandomConnectedGraph r = new RandomConnectedGraph(50);
			System.out.println("Random graph with " + r.nbEdges + " edges and " + r.nbVertices + " vertices");
			test(r, (int) (Math.pow(Math.log(r.nbVertices), 2)) );
			this.myWriter.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
