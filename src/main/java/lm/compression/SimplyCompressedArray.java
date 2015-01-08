package lm.compression;

import com.google.common.primitives.Ints;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

/**
 * Created by Stas on 1/7/15.
 */
public class SimplyCompressedArray {
    private final ByteBuffer buff;
    private final int bytes;
    private final int[] sortedUniques;

    public SimplyCompressedArray(ByteBuffer buff, int bytes, int[] sortedUniques) {
        this.buff = buff;
        this.bytes = bytes;
        this.sortedUniques = sortedUniques;
    }

    public int get(int pos) {
        byte[] res = new byte[4];
        buff.position(pos * bytes);
        buff.get(res, 4 - bytes, bytes);
        return sortedUniques[Ints.fromByteArray(res)];
    }

    public int size() {
        return buff.array().length;
    }
}
