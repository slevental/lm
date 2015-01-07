package lm.graph;

/**
 * Created by Stas on 1/5/15.
 */
public interface Graph<E> {
    void edge(E from, E to);

    boolean isEdge(E from, E to);
}
