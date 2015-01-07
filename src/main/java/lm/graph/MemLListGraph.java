package lm.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stas on 1/5/15.
 */
@SuppressWarnings("unchecked")
class MemLListGraph implements Graph<Integer> {
    private final Set<Integer>[] lst;

    public MemLListGraph(int v) {
        lst = new HashSet[v];
    }

    @Override
    public void edge(Integer from, Integer to) {
        Set<Integer> e = lst[from];
        if (e == null){
            e = newSet();
            lst[from] = e;
        }
        e.add(to);
    }

    private Set<Integer> newSet() {
        return new HashSet<Integer>();
    }

    @Override
    public boolean isEdge(Integer from, Integer to) {
        return false;
    }

    static class Entry<E> {
        final Entry<E> edge;

        public Entry(Entry<E> edge) {
            this.edge = edge;
        }
    }
}
