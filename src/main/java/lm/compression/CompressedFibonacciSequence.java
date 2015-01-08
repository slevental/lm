package lm.compression;

/**
 * Created by Stas on 1/8/15.
 */
public class CompressedFibonacciSequence {
    private final int sequence;
    private final int padding;

    public CompressedFibonacciSequence(int sequence, int padding) {
        this.sequence = sequence;
        this.padding = padding;
    }

    public int getPadding() {
        return padding;
    }

    public int getSequence() {
        return sequence;
    }
}
