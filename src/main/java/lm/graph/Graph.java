package lm.graph;

import java.util.Set;

/**
 * Created by Stas on 1/5/15.
 */
public interface Graph {
    void addEdge(int from, int to);

    Set<Integer> getEdges(int from);

    boolean hasEdge(int from, int to);

    int vertexes();

    int edges();
}
