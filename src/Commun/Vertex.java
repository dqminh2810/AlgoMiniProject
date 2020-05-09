package Commun;

import java.util.ArrayList;

public class Vertex {
    public int id;
    public ArrayList<Edge> neighbours;
    public boolean src, sink;



    public Vertex(int id){
        this.id = id;
        neighbours = new ArrayList<>();
        src = false;
        sink = false;
    }

    public Vertex(int id, boolean source, boolean sink){
        this.id = id;
        neighbours = new ArrayList<>();
        src = source;
        this.sink = sink;
    }

    public boolean equals(Vertex t) {
        return this.id == t.id;
    }
}