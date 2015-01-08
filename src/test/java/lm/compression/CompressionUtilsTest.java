package lm.compression;

import org.junit.Test;

import static java.util.Arrays.stream;
import static org.junit.Assert.*;

public class CompressionUtilsTest {
    @Test
    public void test_is_fibonacci_seq() throws Exception {
        assertTrue(CompressionUtils.isFibonacci(6765));
        assertFalse(CompressionUtils.isFibonacci(6764));
    }

    @Test
    public void test_to_fibonacci_split() throws Exception {
        for (int i = 0; i < 100; i++)
            testNumber(i);
    }

    private void testNumber(int number) {
        long[] fibonacciNums = CompressionUtils.toFibonacciSum(number);
        assertEquals(number, stream(fibonacciNums).sum());
        assertTrue(stream(fibonacciNums).allMatch(CompressionUtils::isFibonacci));

        long[] seq = stream(fibonacciNums).map(CompressionUtils::getFibonacciSeq).toArray();
        if (seq.length == 0) return;

        long prev = seq[0];
        for (int i = 1; i < seq.length; i++) {
            assertTrue(prev - seq[i] > 1);
            prev = seq[i];
        }
        CompressedFibonacciSequence s = CompressionUtils.encodeByFibonacciCode(seq);
        System.out.println(String.format("%" + s.getPadding() + "s", Integer.toBinaryString(s.getSequence())).replace(' ', '0'));
    }
}