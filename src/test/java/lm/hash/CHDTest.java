package lm.hash;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CHDTest {
    @Test
    public void test_perfect_hashing() throws Exception {
        InputStream d = this.getClass().getResourceAsStream("/dict.small.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(d));
        String line;
        Set<String> dict = new HashSet<>();
        while ((line = reader.readLine()) != null) {
            dict.add(line.trim());
        }

        float ratio = 1.03f;
        PerfectHashFunction p = CHD.newBuilder(dict.toArray(new String[dict.size()]))
                .ratio(ratio)
                .buckets((dict.size() / 3) + 31)
                .verbose()
                .build();

        String[] mapped = new String[p.getM()];
        for (String each : dict) {
            int bucket = p.hash(each);
            assertNull(mapped[bucket]);
            mapped[bucket] = each;
        }

        for (String each : mapped) {
            if (each == null) continue;
            assertTrue(dict.contains(each));
        }
    }
}