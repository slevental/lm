package lm.graph;

/**
 * Created by Stas on 1/5/15.
 */
public final class Graphes {
    private Graphes(){}

    public static Graph newMemLListGraph(){
        return new MemLListGraph(1);
    }
}
