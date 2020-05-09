package MinCut;

import Commun.Edge;
import Commun.Vertex;

import java.io.*;
import java.util.*;

public class Graph implements Cloneable {

    private Hashtable<Integer, Vertex> vertices;
    private ArrayList<Edge> edges;
    private String inputFileName;

    public Graph(String inputFileName) throws FileNotFoundException{
        this.inputFileName = inputFileName;
        vertices = new Hashtable<Integer, Vertex>();
        edges = new ArrayList<Edge>();
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

    public Object clone() throws CloneNotSupportedException {
        Graph g = (Graph)super.clone();
        g.edges = new ArrayList<Edge>(this.edges.size());
        g.vertices = new Hashtable<Integer,Vertex>(this.vertices.size());
        for(Map.Entry<Integer, Vertex> entry : this.vertices.entrySet()) {
            Vertex copyV = new Vertex(entry.getKey());
            //Add copyV as a new vertex
            g.vertices.put(copyV.id, copyV);
        }
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

    public void addEdge(Vertex u, Vertex v, int count){
        int idU = u.id;
        int idV = v.id;
        Edge e = new Edge(vertices.get(Math.min(idU, idV)), vertices.get(Math.max(idU, idV)));
        for (int i = 0; i < count; i++){
            vertices.get(idU).neighbours.add(e);
            vertices.get(idV).neighbours.add(e);
            edges.add(e);
        }
    }

    public int removeEdge(Vertex u, Vertex v){
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

    public void randomContract(){
        Random generator = new Random();
        while (vertices.size() > 2){
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
        }
    }

    public void contract(int numberOfContractions) throws FileNotFoundException{
        Random generator = new Random();
        for (int i = numberOfContractions; i > 0 && vertices.size() > 2; i--){
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
        }
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

    private int min(int values[], int length) {
        int minVal = values[0];
        for (int i = 1; i < length; i++) {
            if (values[i] < minVal) {
                minVal = values[i];
            }
        }
        return minVal;
    }

    public int findMinCut() throws FileNotFoundException, CloneNotSupportedException{
        if (this.vertices.size() <= 6) {
            //WARNING: Please change the value of nRepeat to a much smaller one (e.g. n)
            //if you want the program run faster! However, n^2 * ln(n) times of repeat
            //would ensure a (1-1/n) rate of success on finding the minCut.
            int n = this.edges.size();
            int minCut = n;
            int nRepeat = (int) (Math.pow(n, 2) * Math.log(n)); //Please read Warning above!
            //int nRepeat = n; //You can change to this one.
            for (int i = 0; i < nRepeat; i++){
                Graph g = (Graph)this.clone();
                g.randomContract();
                int crossingEdges = g.edges.size();
                if (crossingEdges < minCut){
                    minCut = crossingEdges;
                }
            }
            return minCut;
        }
        else {
            int recursivity = 2;
            int numberOfContracts = (int) (1+vertices.size()/Math.sqrt(2)); // 1+n/srqt(2)
            Graph[] graphList = new Graph[recursivity];
            int[] minList = new int[recursivity];
            for(int i = 0; i < recursivity; i++) {
                graphList[i] = (Graph)this.clone();
                graphList[i].contract(numberOfContracts);
            }
            for(int i = 0; i < recursivity; i++) {
                minList[i] = graphList[i].findMinCut();
            }
            return min(minList, recursivity);
        }
    }


}