package Utils;

import Commun.Edge;
import Commun.Vertex;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Utils {

    public static String generateGraph(int size){
        ArrayList<Vertex> vertices = new ArrayList<>();
        Hashtable<Integer, ArrayList<Edge>> network;

        for(int i=1; i<=size; i++){
            vertices.add(new Vertex(i));
        }
        return null;
    }

    public int getRandomVertexId(int size){
        Random r = new Random();
        return r.nextInt((size+1) - 1) + 1;
    }
    public int getRandomNumberOfAdjacent(int size){
        Random r = new Random();
        return r.nextInt((size+1) - 1) + 1;
    }

    public int getMaximumNbOfAdjacent(int size){
        return size-1;
    }
}
