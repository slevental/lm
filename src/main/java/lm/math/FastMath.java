package lm.math;

/**
 * Created by Stas on 1/21/15.
 */
public final class FastMath {
    private FastMath() {
    }

    public static double exp(double v) {
        final long tmp = (long) (1512775 * v + 1072632447);
        return Double.longBitsToDouble(tmp << 32);
    }

    public static double log(double v) {
        return 6 * (v - 1) / (v + 1 + 4 * (Math.sqrt(v)));
    }

    public static double pow(double a, double b) {
        return exp(b * log(a));
    }
}
