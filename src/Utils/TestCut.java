package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Utils.RandomConnectedGraph;
import FordFukerson.FlowNetwork;
import MinCut.Graph;

public class TestCut {
	
	private FileWriter myWriter;
	private int nbEdges;
	private int nbRecursiveCalls;
	/*
	 * CONSTRUCTORS
	 */
	public TestCut(int edg, int recCalls) {
		try {
			this.myWriter = new FileWriter(new File("results", "results.txt"));
			this.nbEdges = edg;
			this.nbRecursiveCalls = nbRecursiveCalls;
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
	
	private int min(int[] values, int length) {
		int minVal = values[0];
		for(int i = 1; i < length; i++) {
			if(values[i] < minVal) {
				minVal = values[i];
			}
		}
		return minVal;
	}
	
	/*
	 * PUBLIC METHODS
	 */
	
	public void doTest() {
		try	{
			RandomConnectedGraph r = new RandomConnectedGraph(this.nbEdges);
			r.exportTxtFile();
			System.out.println("Random graph with " + r.nbEdges 
					+ " edges and " + r.nbVertices + " vertices generated");
			
			String inputFileName = "resources/randomGraph.txt";
			Graph g = new Graph(inputFileName);
			int numberOfContracts = (int) (1+g.getNumberOfVertices()/Math.sqrt(2));
			
			if (r.nbEdges <= 70) {
				// Check results with Ford Fukerson algorithm
				// Exepected succes = 1/log(r.nbVertices)
				FlowNetwork f = new FlowNetwork(inputFileName);
				int KargerResult = g.findMinCut(this.nbRecursiveCalls, numberOfContracts);
				int FordResult = f.calculateMaxFlowValue();
				if(KargerResult == FordResult) {
					System.out.println("Succes mincut = " + KargerResult);
				}
				else {
					System.out.println("Fail\n"
							+ "Karger result = " + KargerResult
							+ "Ford result = " + FordResult);
				}
			}
			else {
				// Repeat nRepeats times Karger algorithm
				// Expected succes = 1 - 1/n
				int nRepeats =(int) (Math.pow(Math.log(g.getNumberOfVertices()), 2));
				int[] minCuts = new int[nRepeats] ;
				for(int i = 0; i < nRepeats; i++) {
					minCuts[i] = g.findMinCut(this.nbRecursiveCalls, numberOfContracts);
				}
				System.out.println("Karger result = " + min(minCuts, nRepeats));
			}
			this.myWriter.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
