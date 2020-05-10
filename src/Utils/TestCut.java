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
	public TestCut(int edg, int recCalls, String outputFileName) {
		try {
			this.myWriter = new FileWriter(new File("results", outputFileName));
			this.nbEdges = edg;
			this.nbRecursiveCalls = recCalls;
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
			String graphFileName = "randomGraph.txt";
			String inputFileName = "resources/"+graphFileName;

			//Generate graph with given nb of edges and export to graphFileName
			this.generateRandomGraph(nbEdges, graphFileName);

			Graph g = new Graph(inputFileName);

			int numberOfContracts = (int) (1+g.getNumberOfVertices()/Math.sqrt(2));
			int nRepeats =(int) (Math.pow(Math.log(g.getNumberOfVertices()), 2));
			int nRepeats2 =(int) (Math.pow(g.getNumberOfVertices(), 2)*Math.log(g.getNumberOfVertices()));



			if (nbEdges <= 40) {
				// Check results with Ford Fukerson algorithm
				// Exepected succes = 1/log(r.nbVertices)
				FlowNetwork f = new FlowNetwork(inputFileName);
				int FordResult = f.calculateMaxFlowValue();

				for(int i=0; i<nRepeats2; i++){
					int KargerResult = g.findMinCut(this.nbRecursiveCalls, numberOfContracts);
					System.out.println(g.contractionsToString());
					if(KargerResult == FordResult) {
						System.out.println("Succes mincut = " + KargerResult);
					}else{
						System.out.println("Karger result = " + KargerResult + " - " + "Ford result = " + FordResult);
					}
					g = new Graph(inputFileName);
				}
			}
			else {
				// Repeat nRepeats times Karger algorithm
				// Expected succes = 1 - 1/n

				/*
				* For nbRepeats = n²log(n) with n = nbVertices
				* We have
				* P(fail) <= 1/n
				* P(success) >= 1 - 1/n
				* */

				int[] minCuts = new int[nRepeats2] ;
				for(int i = 0; i < nRepeats2; i++) {
					minCuts[i] = g.findMinCut(this.nbRecursiveCalls, numberOfContracts);
					g = new Graph(inputFileName);
					//System.out.println("Karger result for each iteration:  " + minCuts[i]);

				}
				System.out.println("Karger result = " + min(minCuts, nRepeats2));
			}
			this.myWriter.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}


	public void generateRandomGraph(int nbOfEdges, String fileNameExported){
		RandomConnectedGraph r = new RandomConnectedGraph(this.nbEdges);
		r.buildRandomGraph();
		r.exportTxtFile(fileNameExported);
		System.out.println("Random graph with " + r.nbEdges
				+ " edges and " + r.nbVertices + " vertices generated");
		this.nbEdges = r.getNbEdges();
	}
}
