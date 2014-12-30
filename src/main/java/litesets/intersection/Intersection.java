package litesets.intersection;

import com.google.common.collect.*;

import java.util.*;

import static com.google.common.collect.Sets.intersection;

/**
 * Created by Stas on 12/30/14.
 */
public class Intersection {
    public static int THRESHOLD = 10000;

    private Intersection() {
    }

    public static <E extends Comparable> List<E> intersect(Collection<E> left, Collection<E> right, Comparator<E> cmp) {
        if (left == null || right == null)
            return ImmutableList.of();

        if (cmp == null)
            throw new NullPointerException("Comparator cannot be null");
        Ordering<E> ordering = Ordering.from(cmp);


        boolean leftSmaller = left.size() < right.size();
        Collection<E> candidates = leftSmaller ? left : right;
        Collection<E> target = leftSmaller ? right : left;

        if (candidates.size() == 1)
            return singleIntersection(Iterables.getOnlyElement(candidates), target, ordering);

        if (target.size() <= THRESHOLD)
            return simpleIntersection(candidates, target, ordering);

        return complexIntersection(
                ordering.sortedCopy(candidates),
                ordering.sortedCopy(target),
                ordering
        );
    }

    private static <E extends Comparable> List<E> simpleIntersection(Collection<E> candidates, Collection<E> target, Ordering<E> ordering) {
        TreeSet<E> candicatesSet = Sets.newTreeSet(ordering);
        TreeSet<E> targetSet = Sets.newTreeSet(ordering);
        candicatesSet.addAll(candidates);
        targetSet.addAll(target);
        return Lists.newArrayList(intersection(candicatesSet, targetSet));
    }

    private static <E extends Comparable> List<E> singleIntersection(E element, Collection<E> target, Ordering<E> ordering) {
        for (E e : target) {
            if (ordering.compare(e, element) == 0)
                return ImmutableList.of(e);
        }
        return ImmutableList.of();
    }

    private static <E extends Comparable> List<E> complexIntersection(List<E> candidates, List<E> target, Ordering<E> ordering) {
        int succ = 0;
        E prev = null;
        for (Iterator<E> i = candidates.iterator(); i.hasNext(); ) {
            E e = i.next();
            if (prev != null && ordering.compare(prev, e) == 0) {
                i.remove(); // skip duplicates
                continue;
            }
            int pos = binarySearch(target, e, succ, ordering);
            if (pos < 0) i.remove();
            else succ = pos;
            prev = e;
        }
        return candidates;
    }

    /**
     * Copied from {@link java.util.Collections} but extended by adding from parameter
     */
    private static <T> int binarySearch(List<? extends T> l, T key, int from, Comparator<? super T> c) {
        int low = from;
        int high = l.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = l.get(mid);
            int cmp = c.compare(midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }
}
