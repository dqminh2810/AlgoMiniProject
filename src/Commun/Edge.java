package Commun;

public class Edge{
    public Vertex start;
    public Vertex end;

    /*
     * FORD FUKERSON ZONE
     * */
    public boolean passed;
    public int capacity;
    public int flow;
    public Edge returnEdge;
    /*
     * END ZONE
     * */

    public Edge(Vertex u, Vertex v){
        this.start = u;
        this.end = v;
        /*
         * FORD FUKERSON ZONE
         * */
        passed = false;
        capacity = 1;
        flow = 0;
        returnEdge = null;
        /*
         * END ZONE
         * */
    }

    public Edge(Vertex u, Vertex v, int capacity){
        this.start = u;
        this.end = v;
        /*
         * FORD FUKERSON ZONE
         * */
        passed = false;
        this.capacity = capacity;
        flow = 0;
        returnEdge = null;
        /*
         * END ZONE
         * */
    }

    public boolean isSame(Edge e){
        if (e.start == this.start && e.end == this.end){
            return true;
        } else {
            return false;
        }
    }

    public Vertex getAnother(Vertex u){
        if (u == this.start){
            return end;
        } else {
            return this.start;
        }
    }


    /*
     * FORD FUKERSON ZONE
     * */
    public void setPassed(boolean value){
        passed = value;
    }
    public boolean isPassed(){
        return passed;
    }
    /*
     * END ZONE
     * */

}