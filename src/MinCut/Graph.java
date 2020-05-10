package MinCut;

import Commun.Edge;
import Commun.Vertex;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Graph implements Cloneable {

    private Hashtable<Integer, Vertex> vertices;
    private ArrayList<Edge> edges;
    //Key: mincut, Value: list of number of contractions made to arrive to mincut value
    private Hashtable<Integer, ArrayList<Integer>> contractions; 
    private String inputFileName;

    /*
     * CONSTRUCTORS
     */
    public Graph(String inputFileName) throws FileNotFoundException{
        this.inputFileName = inputFileName;
        vertices = new Hashtable<Integer, Vertex>();
        edges = new ArrayList<Edge>();
        contractions = new Hashtable<Integer, ArrayList<Integer>>();
        Scanner in = new Scanner(new File(inputFileName));
        //add all vertices
        while (in.hasNextLine()){
            Scanner line = new Scanner(in.nextLine());
            int id = line.nextInt();
            Vertex v = new Vertex(id);
            vertices.put(id, v);
        }
        in = new Scanner(new File(inputFileName));
        //add edges
        while (in.hasNextLine()){
            Scanner line = new Scanner(in.nextLine());
            int idU = line.nextInt();
            Vertex u = vertices.get(idU);
            while (line.hasNextInt()){
                int idV = line.nextInt();
                Vertex v = vertices.get(idV);
                if (u.id < v.id){
                    addEdge(u, v, 1);
                }
            }
        }
    }
    
    public Graph() {
    	this.edges = new ArrayList<Edge>();
    	this.vertices = new Hashtable<Integer, Vertex>();
    	this.contractions = null;
    	this.inputFileName = "";
    }
    
    /*
     * PRIVATE METHODS
     */
    
    private int min(int values[], int length) {
        int minVal = values[0];
        for (int i = 1; i < length; i++) {
            if (values[i] < minVal) {
                minVal = values[i];
            }
        }
        return minVal;
    }
    
    private void addEdge(Vertex u, Vertex v, int count){
        int idU = u.id;
        int idV = v.id;
        Edge e = new Edge(vertices.get(Math.min(idU, idV)), vertices.get(Math.max(idU, idV)));
        for (int i = 0; i < count; i++){
            vertices.get(idU).neighbours.add(e);
            vertices.get(idV).neighbours.add(e);
            edges.add(e);
        }
    }

    private int removeEdge(Vertex u, Vertex v){
        int count = 0;
        int idU = u.id;
        int idV = v.id;
        Edge e = new Edge(vertices.get(Math.min(idU, idV)), vertices.get(Math.max(idU, idV)));
        for (int i = 0; i < u.neighbours.size(); i++){
            if (u.neighbours.get(i).isSame(e)){
                u.neighbours.remove(i);
                i--;
            }
        }
        for (int i = 0; i < v.neighbours.size(); i++){
            if (v.neighbours.get(i).isSame(e)){
                v.neighbours.remove(i);
                i--;
            }
        }
        for (int i = 0; i < edges.size(); i++){
            if (edges.get(i).isSame(e)){
                edges.remove(i);
                i--;
                count++;
            }
        }
        return count;
    }

    private int randomContract(){
        Random generator = new Random();
        int contractionsDone = 0;
        while (vertices.size() > 2 && edges.size() > 0){
            int index = generator.nextInt(edges.size());
            Edge toRemove = edges.get(index);
            int idV = toRemove.end.id;
            Vertex u = toRemove.start;
            Vertex v = toRemove.end;
            removeEdge(u, v);
            while (v.neighbours.size() > 0){
                Vertex w = v.neighbours.get(0).getAnother(v);
                addEdge(u, w, removeEdge(v, w));
            }
            vertices.remove(idV);
            contractionsDone++;
        }
        return contractionsDone;
    }

    private int contract(int numberOfContractions) throws FileNotFoundException{
        Random generator = new Random();
        int contractionsDone = 0;
        for (int i = numberOfContractions; 
        		i > 0 && vertices.size() > 2 && edges.size() > 0 ; 
        		i--)
        {
            // Contracts g until fixed number of contractions is reached
        	int index = generator.nextInt(edges.size());
            Edge toRemove = edges.get(index);
            int idV = toRemove.end.id;
            Vertex u = toRemove.start;
            Vertex v = toRemove.end;
            removeEdge(u, v);
            while (v.neighbours.size() > 0){
                Vertex w = v.neighbours.get(0).getAnother(v);
                addEdge(u, w, removeEdge(v, w));
            }
            vertices.remove(idV);
            contractionsDone++;
        }
        return contractionsDone;
    }
    
    private Hashtable<Integer,Integer> contractionRepetition(Integer key) {
    	Hashtable<Integer, Integer> contTimes= new Hashtable<Integer, Integer>();
		for(Integer countContr : this.contractions.get(key)) {
			Integer repeated = contTimes.get(countContr);
			if(repeated == null) {
				repeated = 1;
			}
			else {
				repeated++;
			}
			contTimes.put(countContr, repeated);
		}
		return contTimes;
    }
    
    /*
     * PUBLIC METHODS
     */
    // ----- GETERS
    public int getNumberOfVertices() {
    	return this.vertices.size();
    }
    
    public int getNumberOfEdges() {
    	return this.edges.size();
    }
    public Hashtable<Integer, ArrayList<Integer>> getContractions(){
    	return this.contractions;
    }
    
    // ----- END GETERS
    
    
    public String contractionsToString() {
    	String s = "";
    	for(Integer k : this.contractions.keySet()) {
    		s = s + k + ": ";
    		Hashtable<Integer, Integer> contTimes= this.contractionRepetition(k);	
    		Iterator<Entry<Integer, Integer>> itr = contTimes.entrySet().iterator();
    		while(itr.hasNext()) {
    			Entry<Integer, Integer> e = itr.next();
    			Integer contractionsDone = e.getKey();
    			Integer times = e.getValue();
    			s = s + contractionsDone + " (" + times + " times)";
    			if(itr.hasNext()) {
    				s = s + ", ";
    			}
    			else {
    				s = s + "\n";
    			}
    		}
    	}
    	return s;
    }
    
    public Object clone() throws CloneNotSupportedException {
        Graph g = new Graph();
        g.contractions = this.contractions;
        this.vertices.forEach((k,v) -> {
        	Vertex copyV = new Vertex(k);
        	g.vertices.put(k, copyV);
        });
        for(Edge e : this.edges) {
            Vertex u = g.vertices.get(e.start.id);
            Vertex v = g.vertices.get(e.end.id);
            Edge copyE = new Edge(u, v);
            g.edges.add(copyE);
            u.neighbours.add(copyE);
            v.neighbours.add(copyE);
        }
        return g;
    }

    public void printGraph(){
        System.out.println("vertices:");
        Enumeration<Integer> enumKey = vertices.keys();
        while (enumKey.hasMoreElements()){
            Integer id = enumKey.nextElement();
            System.out.print(id + ": ");
            for (Edge e : vertices.get(id).neighbours){
                System.out.print(e.start.id + "-" + e.end.id + " ");
            }
            System.out.println();
        }
        System.out.println("edges:");
        for (Edge e : edges){
            System.out.print(e.start.id + "-" + e.end.id + " ");
        }
        System.out.println();
    }

    /**
     * 
     * @param inRec recursivity asked by user (>= 0)
     * @param inNOC number of contracts asked by user (>= 0)
     * @return min-cut obtained
     * @throws FileNotFoundException
     * @throws CloneNotSupportedException
     */
    public int findMinCut(int inRec, int inNOC) throws FileNotFoundException, CloneNotSupportedException{
        if (this.vertices.size() <= 6 || this.edges.size() == 0) {
            int contractionsDone = 0;     
            contractionsDone = this.randomContract();
            int crossingEdges = this.edges.size();
            ArrayList<Integer> contList = this.contractions.get(crossingEdges);
            if (contList == null) {
            	contList = new ArrayList<Integer>();
            }
            contList.add(contractionsDone);
            this.contractions.put(crossingEdges, contList);
            return crossingEdges;
        }
        else {
            int recursivity = inRec;
            int numberOfContracts = inNOC;
            Graph[] graphList = new Graph[recursivity];
            int[] minList = new int[recursivity];
            int[] contList = new int[recursivity];
            for(int i = 0; i < recursivity; i++) {
                graphList[i] = (Graph)this.clone();
                contList[i] = graphList[i].contract(numberOfContracts);
            }
            for(int i = 0; i < recursivity; i++) {
            	// Recalculate number of contracts in order to adapt it to future 
            	// executions
            	numberOfContracts = (int) (1+graphList[i].vertices.size()/Math.sqrt(2)); // 1+n/srqt(2)
                minList[i] = graphList[i].findMinCut(inRec, numberOfContracts);
            }
            for(int i = 0; i < recursivity; i++) {
            	ArrayList<Integer> listToUpdate = graphList[i].contractions.get(minList[i]);
            	if(listToUpdate != null) {
            		for(int updateIndex = 0; updateIndex < listToUpdate.size(); updateIndex++) {
            			Integer n = listToUpdate.get(updateIndex);
            			listToUpdate.set(updateIndex, n+contList[i]);
                	}
            	}
            	else {
        			listToUpdate = new ArrayList<Integer>();
        			listToUpdate.add(contList[i]);
        		}
            	graphList[i].contractions.put(minList[i], listToUpdate);
            }
            return min(minList, recursivity);
        }
    }
}