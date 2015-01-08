package lm.compression;

import java.nio.ByteBuffer;
import java.util.Map;

import static java.lang.Math.*;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingInt;

/**
 * Created by Stas on 1/7/15.
 */
public class CompressionUtils {
    public static final double GOLDEN_RATIO = (1 + sqrt(5)) / 2;

    public static Integer[] orderByFreq(int[] arr) {
        return stream(arr)
                .parallel()
                .boxed()
                .collect(groupingBy(x -> x, summarizingInt(x -> x)))
                .entrySet()
                .stream()
                .sorted((x, y) -> (int) (y.getValue().getCount() - x.getValue().getCount()))
                .map(Map.Entry::getKey)
                .toArray(Integer[]::new);
    }

    public static long[] toFibonacciSum(long i) {
        return toFibonacciSum(false, new long[64], 0, i, 0);
    }

    public static long[] toFibonacciSumSequences(long i) {
        return toFibonacciSum(true, new long[64], 0, i, 0);
    }

    public static CompressedFibonacciSequence encodeByFibonacciCode(long[] sequences) {
        int encoded = 0;
        if (sequences.length == 0) return null;

        long max = sequences[0];
        for (long sequence : sequences) {
            encoded |= 1 << (max - sequence);
        }
        return new CompressedFibonacciSequence((encoded << 1) + 1, (int) max);
    }

    public static long[] toFibonacciSum(boolean seq, long[] nums, int counter, long l, long prevSeq) {
        if (l < 0)
            return null;

        if (l == 0) {
            long[] result = new long[counter];
            System.arraycopy(nums, 0, result, 0, counter);
            return result;
        }

        int nearestSeq = getFibonacciSeq(l);
        if (prevSeq - nearestSeq == 1)
            nearestSeq--;

        long nearestFibonacci = getFibonacciFast(nearestSeq);
        nums[counter++] = seq ? nearestSeq : nearestFibonacci;
        return toFibonacciSum(seq, nums, counter, l - nearestFibonacci, nearestSeq);
    }

    public static long getFibonacciFast(int n) {
        return (long) Math.floor(pow(GOLDEN_RATIO, n) / sqrt(5) + 0.5);
    }

    public static boolean isFibonacci(long f) {
        return f == 0 || f == 1 || getFibonacciFast(getFibonacciSeq(f)) == f;
    }

    public static int getFibonacciSeq(long f) {
        return (int) Math.floor(Math.log(f * sqrt(5) + 0.5) / Math.log(GOLDEN_RATIO));
    }

    public static SimplyCompressedArray simpleCompression(int[] arr) {
        int[] sortedUniques = stream(arr).sorted().distinct().toArray();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = binarySearch(sortedUniques, arr[i]);
        }

        int max = stream(arr).max().getAsInt();
        int bytes = (int) Math.floor(log(max) / log(16));
        ByteBuffer buff = ByteBuffer.allocate(arr.length * bytes);
        for (int anArr : arr) {
            for (int b = bytes; b > 0; b--) {
                buff.put((byte) (anArr >> (b - 1) * 8));
            }
        }
        return new SimplyCompressedArray(buff, bytes, sortedUniques);
    }
}
