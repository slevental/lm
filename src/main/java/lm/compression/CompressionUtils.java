package lm.compression;

import java.nio.ByteBuffer;
import java.util.Map;

import static java.lang.Math.log;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingInt;

/**
 * Created by Stas on 1/7/15.
 */
public class CompressionUtils {
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

    public static SimplyCompressedArray simpleCompression(int[] arr) {
        int[] sortedUniques = stream(arr).sorted().distinct().toArray();

        for (int i = 0; i < arr.length; i++){
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
