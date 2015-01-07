package lm.hash;

/**
 * A perfect hash function (PHF) maps S ⊆ U to unique values in the range [0, m − 1]. We
 * let n = |S| and u = |U| — note that we must have m ≥ n. A minimal perfect
 * hash function (MPHF) is a PHF with m = n.
 *
 * Created by Stas on 1/5/15.
 */
public interface PerfectHashFunction {
    int hash(String in);

    int getM();
}
