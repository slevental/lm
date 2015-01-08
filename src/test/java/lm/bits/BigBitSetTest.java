package lm.bits;

import org.junit.Test;

import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BigBitSetTest {
    @Test
    public void test_bitset_random() throws Exception {
        int size = 1000000;
        BitSet ideal = new BitSet(size);
        BigBitSet tested = new BigBitSet(size);
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (long i = 0; i < size; i++) {
            int random = rnd.nextInt(size);
            assertEquals(ideal.get(random),tested.get(random));
            ideal.set(random);
            tested.set(random);
        }
    }

    @Test
    public void test_bitset_incrementing() throws Exception {
        long hi = 1000000;
        BigBitSet bs = new BigBitSet(hi + Integer.MAX_VALUE);
        for (long i = 0; i < hi; i++) {
            testBit(bs, Integer.MAX_VALUE + i);
        }
    }

    private void testBit(BigBitSet bs, long bit) {
        assertFalse("Bit " + bit + " is invalid", bs.get(bit));
        bs.set(bit);
        assertTrue("Bit " + bit + " is invalid", bs.get(bit));
    }

}