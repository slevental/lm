package lm.graph;

import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stas on 1/5/15.
 */
@SuppressWarnings("unchecked")
class MemLListGraph implements Graph {
    private final Set<Integer>[] lst;
    private int edges;

    public MemLListGraph(int v) {
        lst = new HashSet[v];
    }

    @Override
    public void addEdge(int from, int to) {
        Set<Integer> e = lst[from];
        if (e == null){
            e = newSet();
            lst[from] = e;
        }
        if (e.add(to)) edges++;
    }

    @Override
    public Set<Integer> getEdges(int from) {
        Set<Integer> s = lst[from];
        if (s == null)
            return ImmutableSet.of();
        return s;
    }

    @Override
    public boolean hasEdge(int from, int to) {
        return getEdges(from).contains(to);
    }

    @Override
    public int vertexes() {
        return lst.length;
    }

    @Override
    public int edges() {
        return edges;
    }

    private Set<Integer> newSet() {
        return new HashSet<>();
    }

}
