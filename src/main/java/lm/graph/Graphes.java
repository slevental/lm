package lm.graph;

/**
 * Created by Stas on 1/5/15.
 */
public final class Graphes {
    private Graphes(){}

    public static Graph newAdjacencyListGraph(int size){
        return new MemLListGraph(size);
    }

    public static GraphPredicate isAcyclic() {
        return new GraphAcyclicPredicate();
    }
}
