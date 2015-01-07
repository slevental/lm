package lm.bits;

/**
 * Created by Stas on 1/5/15.
 */
public class BigBitSet {
    private final static int ADDRESS_BITS_PER_WORD = 6;
    private long[] words;

    public BigBitSet(long size) {
        words = new long[toWordIndex(size - 1) + 1];
    }

    public void set(long bit) {
        int wordIndex = toWordIndex(bit);
        words[wordIndex] |= (1L << bit);
    }

    public boolean get(long bit) {
        int wordIndex = toWordIndex(bit);
        return (words[wordIndex] & (1L << bit)) != 0;
    }

    private int toWordIndex(long bit) {
        return (int) (bit >> ADDRESS_BITS_PER_WORD);
    }
}
