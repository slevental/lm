package lm.hash;

import com.google.common.base.Stopwatch;
import com.google.common.hash.HashFunction;
import lm.bits.BigBitSet;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.common.hash.Hashing.murmur3_32;

/**
 * Created by Stas on 1/5/15.
 */
public class CHD implements PerfectHashFunction {

    private static final int GOOD_FAST_HASH_SEED = (int) System.currentTimeMillis();
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final HashFunction HASH_FUNCTION = murmur3_32(GOOD_FAST_HASH_SEED);

    private final int len;
    private final int buckets;
    private final int[] hashFunctions;

    public CHD(int len, int buckets, int[] hashFunctions) {
        this.len = len;
        this.buckets = buckets;
        this.hashFunctions = hashFunctions;
    }

    public static CHD.Builder newBuilder(String[] objects) {
        return new CHD.Builder(objects);
    }

    public int hash(String in) {
        return fnvHash(hashFunctions[fnvHash(0, in) % buckets], in) % len;
    }

    @Override
    public int getM() {
        return len;
    }

    static class Builder {
        private final String[] obj;
        private int buckets;
        private float ratio = 1.0f;
        private boolean verbose;

        Builder(String[] obj) {
            this.obj = obj;
            this.buckets = obj.length / 5;
        }

        public Builder buckets(int b) {
            this.buckets = b;
            return this;
        }

        public Builder ratio(float r) {
            this.ratio = r;
            return this;
        }

        public Builder verbose(){
            this.verbose = true;
            return this;
        }

        public PerfectHashFunction build() {
            int len = obj.length;
            int targetLen = Math.round(len * ratio);

            int r = buckets;
            int[] hashFunctions = new int[r];
            Node[] buckets = new Node[r];
            BigBitSet mapped = new BigBitSet(targetLen);

            for (String each : obj) {
                int hashCode = fnvHash(0, each);
                int bucketPos = hashCode % r;
                Node bucket = buckets[bucketPos];
                buckets[bucketPos] = new Node(bucket, each, bucket == null ? 1 : bucket.size + 1, bucketPos);
            }

            Arrays.sort(buckets, (o1, o2) -> o1 == o2 ? 0 : o1 == null ? -1 :
                    o2 == null ? 1 :
                            o2.size - o1.size);

            Stopwatch s = Stopwatch.createStarted();
            int collisions = 0;

            for (int i = 0; i < buckets.length; i++) {
                Node node = buckets[i];
                if (node == null) continue;
                int d = 1;


                if (this.verbose && i % 10000 == 0) {
                    System.out.println("Processed : "
                            + i
                            + " of "
                            + buckets.length
                            + " (took "
                            + s.elapsed(TimeUnit.SECONDS)
                            + "s, collisions = "
                            + collisions
                            + ")");

                    s.reset();
                    s.start();
                    collisions = 0;
                }

                Node n = node;
                BitSet current = new BitSet(node.size);
                int[] hashes = new int[node.size];

                while (n != null) {
                    int h = fnvHash(d, n.str) % targetLen;
                    if (!mapped.get(h) && !current.get(h)) {
                        hashes[n.size - 1] = h;
                        current.set(h);
                    } else {
                        current.clear();
                        n = node;
                        d++;
                        collisions++;
                        continue;
                    }
                    n = n.next;
                }
                hashFunctions[node.original] = d;
                for (int hash : hashes) {
                    mapped.set(hash);
                }
            }
            if (verbose){
                System.out.println("Hash functions = " + hashFunctions.length);
                Set<Object> func = new HashSet<>();
                for (int each : hashFunctions) {
                    func.add(each);
                }
                System.out.println("Unique hash functions = " + func.size());
            }

            return new CHD(targetLen, this.buckets, hashFunctions);
        }

        static class Node {
            final Node next;
            final String str;
            final int size;
            final int original;

            Node(Node next, String obj, int size, int original) {
                this.next = next;
                this.str = obj;
                this.size = size;
                this.original = original;
            }

        }
    }

    private static int fnvHash(int d, String hash) {
        if (d == 0)
            return HASH_FUNCTION.hashString(hash, CHARSET).asInt() & 0x7fffffff;
        int base = 0x11FACE8D;
        return murmur3_32(d * base).hashString(hash, CHARSET).asInt() & 0x7fffffff;
    }


}
