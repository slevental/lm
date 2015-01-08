package lm.graph;

import java.util.Stack;

/**
 * Created by Stas on 1/8/15.
 */
public class GraphAcyclicPredicate implements GraphPredicate {

    @Override
    public boolean apply(Graph graph) {
        int size = graph.vertexes();
        boolean[] marked = new boolean[size];
        boolean[] stacked = new boolean[size];
        int[] edgeTo = new int[size];
        Stack<Integer> cycle = new Stack<>();

        for (int v = 0; v < size; v++)
            if (!marked[v]) {
                dfs(cycle, marked, stacked, edgeTo, graph, v);
            }

        if (!cycle.isEmpty()){
            int first = -1, last = -1;
            for (int v : cycle) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                return false;
            }
        }

        return true;
    }

    private void dfs(Stack<Integer> cycle, boolean[] marked, boolean[] stacked, int[] edgeTo, Graph g, int v) {
        stacked[v] = true;
        marked[v] = true;
        for (int w : g.getEdges(v)) {
            if (!cycle.isEmpty()) return;
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(cycle, marked, stacked, edgeTo, g, w);
            } else if (stacked[w]) {
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
        stacked[v] = false;
    }
}
