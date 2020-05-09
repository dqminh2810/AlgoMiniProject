package FordFukerson;

import Commun.Edge;
import Commun.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class FlowNetwork {
    public Hashtable<Integer, Vertex> vertices;
    private ArrayList<Edge> edges;
    private String inputFileName;
    private Hashtable<Integer, ArrayList<Edge>> network;


    public FlowNetwork(String inputFileName) throws FileNotFoundException {
        this.inputFileName = inputFileName;
        vertices = new Hashtable<Integer, Vertex>();
        edges = new ArrayList<Edge>();
        network = new Hashtable<>();
        Scanner in = new Scanner(new File(inputFileName));
        //add all vertices
        while (in.hasNextLine()) {
            Scanner line = new Scanner(in.nextLine());
            int id = line.nextInt();
            Vertex v = new Vertex(id);
            vertices.put(id, v);
            network.put(id, new ArrayList<>());
        }
        in = new Scanner(new File(inputFileName));
        //add edges
        while (in.hasNextLine()) {
            Scanner line = new Scanner(in.nextLine());
            int idU = line.nextInt();
            Vertex u = vertices.get(idU);
            while (line.hasNextInt()) {
                int idV = line.nextInt();
                Vertex v = vertices.get(idV);
                if (u.id < v.id) {
                    addEdge(u, v, 1);
                }
            }
        }
    }

    public void addEdge(Vertex start, Vertex end, int count) {
        int idStart = start.id;
        int idEnd = end.id;
        Edge newEdge = new Edge(vertices.get(idStart), vertices.get(idEnd), 1);
        //Vertex vertex = vertices.get(start.id);
        //Vertex returnVertex = vertices.get(end.id);
        Edge returnEdge = new Edge(end, start);
        newEdge.returnEdge = returnEdge;
        returnEdge.returnEdge = newEdge;

        for (int i = 0; i < count; i++) {
            vertices.get(idStart).neighbours.add(newEdge);
            vertices.get(idEnd).neighbours.add(newEdge);
            edges.add(newEdge);
            network.get(start.id).add(newEdge);
            edges.add(newEdge.returnEdge);
            network.get(end.id).add(returnEdge);


        }
    }


    public Vertex getSource(){
        for(int key: vertices.keySet()){
            if(vertices.get(key).src)
                return vertices.get(key);
        }
        return null;
    }

    public Vertex getSink(){
        for(int key: vertices.keySet()){
            if(vertices.get(key).sink)
                return vertices.get(key);
        }
        return null;
    }

    public Vertex getVertex(int id){
        for(int key: vertices.keySet()){
            if(id == key)
                return vertices.get(key);
        }
        return null;
    }

    public boolean isVertexInNetwork(int id){
        for(int key: vertices.keySet()){
            if(id == key)
                return true;
        }
        return false;
    }

    public Edge getEdge(int startId, int endId){
        for(Edge e : network.get(startId)){
            if(e.end.id == endId) return e;
        }
        return null;
    }

    public ArrayList<Edge> getEdges(){
        ArrayList<Edge> edges = new ArrayList<>();
        for(int key: network.keySet()){
           edges.addAll(network.get(key));
        }
        return edges;
    }

    public boolean isNeighbours(Vertex v, Edge e){
        return v.neighbours.contains(e);
    }

    public ArrayList<Edge> getPath(Vertex start, Vertex end, ArrayList<Edge> path){
        ArrayList<Edge> neighboursSrc = start.neighbours;
        ArrayList<Edge> neighboursSrcInPath = new ArrayList<>();

        for(Edge e: path){
            if(neighboursSrc.contains(e)){
                neighboursSrcInPath.add(e);
                //neighboursSrcInPath.add(e.returnEdge);
            }
        }

        if(start.id == end.id) return path;

        for(Edge e: network.get(start.id)){
            //int residualCapacity = e.capacity - e.flow;
            if(isCircle(path)){
                path.clear();
            }
            if(!e.passed && !e.returnEdge.passed ){
                //if(neighboursSrcInPath.size() < 2){
                    path.add(e);
                    getEdge(e.start.id, e.end.id).passed = true;
                    getEdge(e.end.id, e.start.id).passed = true;
                    ArrayList<Edge> result = getPath(e.end, end, path);
                    if(result != null){
                        /*for(Edge ed: result){
                            System.out.println(ed.start.id + " - " + ed.end.id);
                        }*/
                        return result;
                    }else{
                        getEdge(e.start.id, e.end.id).passed = false;
                        getEdge(e.end.id, e.start.id).passed = false;
                    }
                //}
            }
        }
        return null;
    }

    public int calculateMaxFlow(){
        Vertex source = getSource();
        Vertex sink = getSink();
        if(source == null && sink == null){
            return -1;
        }
        ArrayList<Edge> path = getPath(source, sink, new ArrayList<Edge>());

        while(path != null){
            for(Edge e : path){
                e.flow = 1;
                //e.returnEdge.flow -= flow;
            }
            //System.out.print(printPath(path));
            path = getPath(source, sink, new ArrayList<Edge>());
        }
        int sumFlow = 0;
        for(Edge e: network.get(getSource().id)){
            /*if(e.flow != 0){
                System.out.println(e.start.id + " ____ " + e.end.id);
            }*/
            sumFlow+=e.flow;
        }

        return sumFlow;
    }

    public void setSrcSink(int srcId, int sinkId){
        vertices.get(srcId).src = true;
        vertices.get(sinkId).sink = true;
    }

    public int calculateMaxFlowValue() {
        ArrayList<Flow> flows = new ArrayList<>();
        ArrayList<Integer> finalFlows = new ArrayList<>();
        for (int i = 1; i < vertices.size()+1; i++) {
            for (int j = 1; j < vertices.size()+1; j++) {
                resetAll();
                if (i != j){
                    setSrcSink(i, j);
                    if(!getVertex(i).src || !getVertex(j).sink) System.out.println("src sink error");
                    //System.out.println("src: "+i +" - sink: " + j);
                    flows.add(new Flow(i, j, calculateMaxFlow()));        //Calculate max flow for each pair s & t in graph with conditional s != t
                    //System.out.println("src: "+i +" - sink: " + j);
                    //System.out.println(calculateMaxFlow());
                   //System.out.println();
                }
            }
        }
        for(Flow f1: flows){
            for(Flow f2: flows){
                if(f1.sourceId == f2.sinkId && f1.sinkId == f2.sourceId){
                    finalFlows.add(Math.max(f1.value, f2.value));
                }
            }
        }
        return Collections.min(finalFlows);
    }


    public void resetAll(){
        for(Edge e: edges){
            e.setPassed(false);
        }

        for(int key: vertices.keySet()){
            vertices.get(key).src = false;
            vertices.get(key).sink = false;
        }

        for(int key: network.keySet()){
            for(Edge e: network.get(key)){
                e.capacity = 1;
                e.flow = 0;
                e.passed = false;
            }
        }
    }

    public String printPath(ArrayList<Edge> path){
        String res = "";
        for(int i=0; i<path.size(); i++){
            if(i==path.size()-1){
                res+=(path.get(i).start.id+"-"+path.get(i).end.id+"\n");
            }else{
                res+=(path.get(i).start.id+"-"+path.get(i).end.id+" , ");

            }
        }
        return res;
    }

    public boolean isCircle(ArrayList<Edge> path){
        if(path.size() != 0){
            Vertex start = path.get(0).start;
            Vertex end = path.get(path.size()-1).end;
            return start.id == end.id;
        }
        return false;
    }






// Old algo

    /*
     * FORD-FUKERSON ZONE
     * */
/*
    public int calculateMaxFlowValue() {
        ArrayList<Integer> flows = new ArrayList<>();
        for (int i = 1; i < vertices.size()+1; i++) {
            for (int j = 1; j < vertices.size()+1; j++) {
                resetAllNotPassed();
                Hashtable<Integer, Vertex> tmpVertices = new Hashtable<>(vertices);
                if (i != j){
                    //flows.add(calculateMaxFlowValue(tmpVertices.get(i), tmpVertices.get(j)));        //Calculate max flow for each pair s & t in graph with conditional s != t
                    flows.add(fastFlow(tmpVertices.get(i), tmpVertices.get(j)));        //Calculate max flow for each pair s & t in graph with conditional s != t
                }
            }
        }
        return Collections.min(flows);
    }

    public int calculateMaxFlowValue(Vertex source, Vertex sink) {
        ArrayList<ArrayList<Edge>> paths = new ArrayList<>();   //All the paths (set of edges for each path) possible from source to sink
        int[] counts;   //Count how many path considered from source
        ArrayList<Edge> path = new ArrayList<>();
        counts =  new int[vertices.size()+1];
        vertices.forEach((k,v)->counts[k]=0);



        while(counts[source.id] < source.neighbours.size()){
            System.out.println("source: " + source.id + " sink: " + sink.id);
            findPaths(source, sink, paths, counts, path);
            //System.out.println(printPath(path));
            printAllPaths(paths);
        }


        //printAllPaths(paths);

        return paths.size();
    }

    public void findPaths(Vertex from, Vertex sink, ArrayList<ArrayList<Edge>> paths, int[] counts, ArrayList<Edge> path){
        while(counts[from.id] < from.neighbours.size()){
            Edge nextMove = findNextMove(from, counts);     //First from is always source

            if(nextMove!=null){
                path.add(nextMove);
                if(nextMove.getAnother(from).equals(sink)){     //if path reach the sink
                    ArrayList<Edge> tmp = new ArrayList<>(path);

                    paths.add(tmp);                            //add it to paths collection
                    path.clear();
                    //break;

                }else{
                    findPaths(nextMove.getAnother(from), sink, paths, counts, path);
                }
            }
        }

    }

    public Edge findNextMove(Vertex from, int[] counts){
        Edge nextMove = null;
        if(counts[from.id] < from.neighbours.size()){
            if(!from.neighbours.get(counts[from.id]).isPassed() ){
                //System.out.println("from id: "+from.id);
                nextMove = from.neighbours.get(counts[from.id]);
                from.neighbours.get(counts[from.id]).setPassed(true);
            }
        }
        counts[from.id]+=1;
        return nextMove;
    }

    public String printPath(ArrayList<Edge> path){
        String res = "";
        for(int i=0; i<path.size(); i++){
            if(i==path.size()-1){
                res+=(path.get(i).start.id+"-"+path.get(i).end.id+"\n");
            }else{
                res+=(path.get(i).start.id+"-"+path.get(i).end.id+" , ");

            }
        }
        return res;
    }

    public void printAllPaths(ArrayList<ArrayList<Edge>> paths){
        String res = "paths:\n";
        for(ArrayList<Edge> path: paths){
            res+=printPath(path);
        }
        System.out.println(res);
    }

    public void resetAllNotPassed(){
        for(Edge e: edges){
            e.setPassed(false);
        }
    }


    public int fastFlow(Vertex src, Vertex sink){
        return Math.min(src.neighbours.size(), sink.neighbours.size());
    }
    */
    /*
     * END ZONE
     * */

}
