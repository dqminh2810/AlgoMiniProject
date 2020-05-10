package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import Utils.RandomConnectedGraph;
import FordFukerson.FlowNetwork;
import MinCut.Graph;

public class TestCut {

	private String randomGraphFileName;
	private String resultFileName;
	private int nbVertices;
	private int nbEdges;
	private int nbRecursiveCalls;
	private int nRepeats;
	private int nbContractions;

	private ArrayList<Integer> results;
	/*
	 * CONSTRUCTORS
	 */
	public TestCut(int edg, int recCalls, String randomGraphFileName, String resultFileName) {
		try {
			this.nbEdges = edg;
			this.nbRecursiveCalls = recCalls;
			nbVertices = 0;
			nRepeats = 0;
			results = new ArrayList<>();
			this.randomGraphFileName = randomGraphFileName;
			this.resultFileName = resultFileName;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
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
			String inputFileName = "resources/"+randomGraphFileName;

			//Generate graph with given nb of edges and export to graphFileName
			this.generateRandomGraph(nbEdges, randomGraphFileName);

			Graph g = new Graph(inputFileName);

			int numberOfContracts = (int) (1+g.getNumberOfVertices()/Math.sqrt(2));
			//int nRepeats =(int) (Math.pow(Math.log(g.getNumberOfVertices()), 2));
			nRepeats =(int) (Math.pow(g.getNumberOfVertices(), 2)*Math.log(g.getNumberOfVertices()));



			if (nbEdges <= 40) {
				// Check results with Ford Fukerson algorithm
				// Exepected succes = 1/log(r.nbVertices)
				FlowNetwork f = new FlowNetwork(inputFileName);
				int FordResult = f.calculateMaxFlowValue();

				int KargerResult = g.findMinCut(this.nbRecursiveCalls, numberOfContracts);

				boolean equalValue = false;

				for(int i=0; i<nRepeats; i++){
					KargerResult = g.findMinCut(this.nbRecursiveCalls, numberOfContracts);
					results.add(KargerResult);
					if(KargerResult == FordResult) {
						System.out.println("Succes mincut = " + KargerResult);
					}else{
						//System.out.println("Karger result = " + KargerResult + " - " + "Ford result = " + FordResult);
					}
					g = new Graph(inputFileName);
				}


				results.add(Collections.min(results));
				results.add(-1);
				results.add(FordResult);
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

				int[] minCuts = new int[nRepeats] ;
				for(int i = 0; i < nRepeats; i++) {
					minCuts[i] = g.findMinCut(this.nbRecursiveCalls, numberOfContracts);
					results.add(minCuts[i]);
					//System.out.println("Karger result for each iteration:  " + minCuts[i]);
					g = new Graph(inputFileName);
				}
				results.add(Collections.min(results));
				System.out.println("Karger result = " + min(minCuts, nRepeats));
			}
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
		this.nbVertices = r.getNbVertices();
	}


	public void exportResult(){
		try {
			FileWriter myWriter = new FileWriter(new File("results", resultFileName));
			myWriter.write(nbVertices+"\n");
			myWriter.write(nbEdges+"\n");
			myWriter.write(nbRecursiveCalls+"\n");
			myWriter.write(nRepeats+"\n");
			myWriter.write("\n");

			for(int r: results){
				if(r==-1) myWriter.write("\n");
				else myWriter.write(r+"\n");
			}
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
